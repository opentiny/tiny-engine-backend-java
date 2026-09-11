#!/bin/bash
# ============================================================
# checkstyle-pr.sh - 增量检查（扫描整个变更文件，不过滤行号）
# 功能：对本次提交中变更的 Java 文件执行完整的 Checkstyle 检查
# 不阻断构建，生成完整报告
# ============================================================

set -e
unset GREP_OPTIONS
echo "========================================"
echo "  Checkstyle 增量检查"
echo "  扫描范围：本次变更的 Java 文件（完整文件）"
echo "========================================"

# 1. 确定比较基线
if [ -n "${GITHUB_BASE_REF:-}" ]; then
    BASE_BRANCH="origin/$GITHUB_BASE_REF"
elif [ "${GITHUB_EVENT_NAME:-}" == "push" ]; then
    BASE_BRANCH="${EVENT_BEFORE:-}"
    HEAD_COMMIT="${EVENT_SHA:-${GITHUB_SHA:-HEAD}}"
    if [ -z "$BASE_BRANCH" ] || [ "$BASE_BRANCH" == "0000000000000000000000000000000000000000" ]; then
        BASE_BRANCH="FULL_SCAN"
    fi
else
    if git rev-parse --verify origin/main >/dev/null 2>&1; then
        BASE_BRANCH="origin/main"
    elif git rev-parse --verify origin/develop >/dev/null 2>&1; then
        BASE_BRANCH="origin/develop"
    else
        echo "❌ 无法确定目标分支，请设置 BASE_BRANCH 环境变量。"
        exit 1
    fi
    echo "🔍 本地运行模式，对比分支: $BASE_BRANCH"
fi

# 2. 获取变更的 Java 文件
if [ "$BASE_BRANCH" == "FULL_SCAN" ]; then
    CHANGED_FILES=$(find . -type f \( -path '*/src/main/java/*.java' -o -path '*/src/test/java/*.java' \) -print | sed 's|^./||' | sort)
elif [ "${GITHUB_EVENT_NAME:-}" == "push" ]; then
    if ! CHANGED_FILES=$(git diff --name-only --diff-filter=ACMRT "$BASE_BRANCH" "$HEAD_COMMIT" -- '*.java'); then
        echo "❌ 无法比较提交 $BASE_BRANCH 与 $HEAD_COMMIT。"
        exit 1
    fi
else
    if ! CHANGED_FILES=$(git diff --name-only --diff-filter=ACMRT "$BASE_BRANCH"...HEAD -- '*.java'); then
        echo "❌ 无法比较基线 $BASE_BRANCH 与 HEAD。"
        exit 1
    fi
fi

if [ -z "$CHANGED_FILES" ]; then
    echo "✅ 没有 Java 文件变更，跳过检查。"
    exit 0
fi

echo "📝 变更的 Java 文件："
echo "$CHANGED_FILES"
echo "----------------------------------------"

# 按模块分组，并把路径转换为 Checkstyle includes 使用的源码相对路径
declare -A module_files
for file in $CHANGED_FILES; do
    module="${file%%/*}"
    if [ -z "$module" ] || [ "$module" == "$file" ]; then
        echo "⚠️ 忽略根目录文件: $file"
        continue
    fi
    rel="${file#$module/}"
    case "$rel" in
        src/main/java/*)
            include="${rel#src/main/java/}"
            ;;
        src/test/java/*)
            include="${rel#src/test/java/}"
            ;;
        *)
            echo "⚠️ 跳过非源码目录 Java 文件: $file"
            continue
            ;;
    esac
    if [ -z "${module_files[$module]}" ]; then
        module_files[$module]="$include"
    else
        module_files[$module]="${module_files[$module]},$include"
    fi
done

if [ ${#module_files[@]} -eq 0 ]; then
    echo "⚠️ 没有识别到任何模块，跳过检查。"
    exit 0
fi

echo "📝 按模块分组后的相对路径："
for module in "${!module_files[@]}"; do
    echo "  $module: ${module_files[$module]}"
done
echo "----------------------------------------"

total_violations=0
execution_failures=0

# 对每个模块执行 Checkstyle
for module in "${!module_files[@]}"; do
    file_list="${module_files[$module]}"
    report_file="$module/target/checkstyle-result.xml"
    echo "🚀 扫描模块: $module"
    echo "   文件列表: $file_list"

    if [ ! -d "$module" ] || [ ! -f "$module/pom.xml" ]; then
        echo "⚠️ 模块目录 $module 不存在或没有 pom.xml，跳过。"
        continue
    fi

    echo "   - 运行 Checkstyle 检查（增量扫描）..."
    echo "$file_list"
    set +e
    PROJECT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || pwd)
    rm -f "$report_file"
    output=$(cd "$module" && \
        echo "   Current directory: $(pwd)" && \
        echo "   Checking file existence:" && \
        IFS=',' read -ra includes <<< "$file_list" && \
        for include in "${includes[@]}"; do \
            if [ -f "src/main/java/$include" ]; then \
                ls -l "src/main/java/$include"; \
            elif [ -f "src/test/java/$include" ]; then \
                ls -l "src/test/java/$include"; \
            else \
                echo "   ⚠️  not found: $include"; \
            fi; \
        done && \
        mvn -f "$PROJECT_ROOT/pom.xml" -pl "$module" checkstyle:check \
            -Dcheckstyle.config.location="$PROJECT_ROOT/checkstyle/code-check-checkstyle.xml" \
            -Dcheckstyle.violationSeverity=warning \
            -Dcheckstyle.outputFormat=xml \
            -Dcheckstyle.includes="$file_list" 2>&1 )
    mvn_exit=$?
    if [ $mvn_exit -ne 0 ]; then
        echo "   ⚠️ 模块 $module 的 Checkstyle 检查失败（但继续）"
    fi
    set -e
    echo "$output"

    # 从 XML 报告中统计违规数，比解析 Maven 日志更稳定
    if [ -f "$report_file" ]; then
        count=$(grep -c -- '<error ' "$report_file" 2>/dev/null || true)
    else
        count=0
    fi
    total_violations=$((total_violations + count))
    echo "   模块 $module 违规数: $count"

    if [ $mvn_exit -ne 0 ] && [ "$count" -eq 0 ]; then
        echo "   ❌ 模块 $module 的 Checkstyle 执行失败，且未生成可解析的违规报告。"
        execution_failures=$((execution_failures + 1))
    fi

    # （可选）生成 HTML 报告供人工查看
    echo "   - 生成 HTML 报告（可选）..."
    set +e
    (cd "$module" && mvn -f "$PROJECT_ROOT/pom.xml" -pl "$module" checkstyle:checkstyle \
        -Dcheckstyle.config.location="$PROJECT_ROOT/checkstyle/code-check-checkstyle.xml" \
        -Dcheckstyle.includes="$file_list" \
        -Dcheckstyle.violationSeverity=warning) > /dev/null 2>&1
    set -e
    echo ""
done

# 汇总输出
echo "----------------------------------------"
if [ $total_violations -eq 0 ]; then
    echo "✅ 所有变更文件未发现违规！"
else
    echo "⚠️ 总计发现 $total_violations 个违规。"
    echo ""
    echo "📋 违规摘要（前 30 条）："
    for module in "${!module_files[@]}"; do
        report_file="$module/target/checkstyle-result.xml"
        if [ -f "$report_file" ] && grep -q -- '<error' "$report_file" 2>/dev/null; then
            grep -- '<error' "$report_file" 2>/dev/null | head -30 | sed 's/<error //; s/\/>//' | \
                sed 's|line="|行号: |g; s|column="|列: |g; s|severity="|严重性: |g; s|message="|信息: |g; s|source="||g' | \
                while read -r line; do
                    echo "  $line"
                done || true
            break
        fi
    done
fi

# Step Summary
if [ -n "$GITHUB_STEP_SUMMARY" ]; then
    {
        echo "## 📋 Checkstyle 汇总报告"
        echo ""
        echo "| 指标 | 结果 |"
        echo "|------|------|"
        if [ $total_violations -eq 0 ]; then
            echo "| 总违规数 | ✅ **0** |"
        else
            echo "| 总违规数 | ⚠️ **$total_violations** |"
        fi
        echo "| 执行失败模块数 | $execution_failures |"
        echo "| 涉及模块 | ${!module_files[*]} |"
        echo ""
        echo "📥 完整报告已作为 Artifact 上传。"
    } >> "$GITHUB_STEP_SUMMARY"
fi

# 根据违规数决定退出码
if [ $total_violations -eq 0 ] && [ $execution_failures -eq 0 ]; then
    echo "✅ 检查通过，构建成功。"
    exit 0
elif [ $execution_failures -ne 0 ]; then
    echo "❌ 有 $execution_failures 个模块 Checkstyle 执行失败，构建失败。"
    exit 1
else
    echo "❌ 发现 $total_violations 个违规，构建失败。"
    exit 1
fi

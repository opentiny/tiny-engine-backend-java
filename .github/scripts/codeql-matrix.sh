#!/bin/bash

set -euo pipefail

java_build_mode="${1:-autobuild}"

matrix_entries=""

has_files() {
    git ls-files "$@" | grep . >/dev/null
}

add_entry() {
    local entry="$1"
    if [ -n "$matrix_entries" ]; then
        matrix_entries="$matrix_entries,$entry"
    else
        matrix_entries="$entry"
    fi
}

if has_files '.github/workflows/*.yml' '.github/workflows/*.yaml'; then
    add_entry '{"language":"actions","build-mode":"none"}'
fi

if has_files '*.java'; then
    add_entry "{\"language\":\"java-kotlin\",\"build-mode\":\"$java_build_mode\"}"
fi

if has_files '*.js' '*.jsx' '*.ts' '*.tsx' '*.mjs' '*.cjs' '*.vue' '*.html'; then
    add_entry '{"language":"javascript-typescript","build-mode":"none"}'
fi

printf '{"include":[%s]}\n' "$matrix_entries"

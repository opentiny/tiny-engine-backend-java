const {generateCode} = require('@opentiny/tiny-engine-dsl-vue');


function command(pageInfo, blocksData) {
    var result = generateCode({pageInfo: pageInfo, blocksData: blocksData});
    return JSON.stringify(result);
}

function pageCommand(pageInfo, blocksData, componentsMap) {
    var result = generateCode({pageInfo: pageInfo, blocksData: blocksData, componentsMap: componentsMap});
    return JSON.stringify(result);
}

if (process.argv.length <= 4) {
    var pageInfo = JSON.parse(process.argv[2]);
    var blocksData = JSON.parse(process.argv[3]);
    console.log(command(pageInfo, blocksData));
} else if (process.argv.length > 4) {
    var pageInfo = JSON.parse(process.argv[2]);
    var blocksData = JSON.parse(process.argv[3]);
    var componentsMap = JSON.parse(process.argv[4]);
    console.log(pageCommand(pageInfo, blocksData, componentsMap));
} else {
    console.log("Usage: node tiny-engine-dsl.js");
}
const Transformer = require('@opentiny/tiny-engine-transform').default;

function command(codes) {
    var result = Transformer.translate(codes);
    return JSON.stringify(result);
}

if (process.argv.length <= 4) {
    // Collect all arguments except the first two (node and script path)
    const codes = process.argv.slice(2).join(" ");
    console.log(command(codes));
} else {
    console.log("Usage: node tiny-engine-transform.js <codes>");
}
config.output.library = 'ktjs_example04_lib';

// puts our functions in the global (window) scope. Plain works, AMD not
// window["ktjs_example04_lib"] = ...
//config.output.libraryTarget = 'window';

// Creates a `ktjs_example04_lib`. Plain works, AMD not
// ktjs_example04_lib = ...
// config.output.libraryTarget = 'var'; // ktjs_example04.bar()

// Creates a `ktjs_example04_lib`. Plain works, AMD not
// this["ktjs_example04_lib"] = ...
// config.output.libraryTarget = 'this';

// Creates a `ktjs_example04_lib`. Plain works, AMD works!
// ktjs_example04_lib = ...
config.output.libraryTarget = 'umd';

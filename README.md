# Compiler Plugin Reference

A compiler plugin to export declarations, and a gradle plugin to generate IR references to those declarations.  Resolution is based on FqNames and IdSignatures.

Currently WIP.

Will replace the naming parts of  [compiler-plugin-utils](https://github.com/rnett/compiler-plugin-utils) once it is done.

Examples of usage and generated code are available in [test/runtime](https://github.com/rnett/compiler-plugin-reference/blob/main/test/runtime/src/commonMain/kotlin/tester/second/TestDeclarations.kt) (see it's other platforms, too) and [test/compiler-plugin](https://github.com/rnett/compiler-plugin-reference/blob/main/test/compiler-plugin/src/main/kotlin/test/generation/Names.kt), respectively.

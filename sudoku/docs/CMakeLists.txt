set(DOXYGEN_EXTRACT_ALL YES)
set(DOXYGEN_BUILTIN_STL_SUPPORT YES)

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
    "${Sudoku_SOURCE_DIR}/include/sudoku/*.h"
    "${Sudoku_SOURCE_DIR}/include/sudoku/*.hpp"
    "${Sudoku_SOURCE_DIR}/include/sudoku/*.cc"
    "${Sudoku_SOURCE_DIR}/include/sudoku/*.cpp")

doxygen_add_docs(docs
    ${SOURCE_LIST}
    "${CMAKE_CURRENT_SOURCE_DIR}/README.md"
    WORKING_DIRECTORY
    "${PROJECT_SOURCE_DIR}/include"
)

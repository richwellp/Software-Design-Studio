# Note that headers are optional, and do not affect add_library, but they will not
# show up in IDEs unless they are listed in add_library.

# Optionally glob, but only for CMake 3.12 or later:
file(GLOB HEADER_LIST CONFIGURE_DEPENDS
        "${Sudoku_SOURCE_DIR}/include/sudoku/*.h"
        "${Sudoku_SOURCE_DIR}/include/sudoku/*.hpp")

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
        "${Sudoku_SOURCE_DIR}/src/*.h"
        "${Sudoku_SOURCE_DIR}/src/*.hpp"
        "${Sudoku_SOURCE_DIR}/src/*.cc"
        "${Sudoku_SOURCE_DIR}/src/*.cpp")

# Make an automatic library - will be static or dynamic based on user setting
add_library(sudoku ${SOURCE_LIST} ${HEADER_LIST} sudoku.cpp)

# We need this directory, and users of our library will need it too
target_include_directories(sudoku PUBLIC ../include)

# All users of this library will need at least C++11
target_compile_features(sudoku PUBLIC cxx_std_11)

# Cross-platform compiler lints
if (${CMAKE_CXX_COMPILER_ID} STREQUAL "Clang"
        OR ${CMAKE_CXX_COMPILER_ID} STREQUAL "GNU")
    target_compile_options(sudoku PRIVATE
            -Wall
            -Wextra
            -Wswitch
            -Wconversion
            -Wparentheses
            -Wfloat-equal
            -Wzero-as-null-pointer-constant
            -Wpedantic
            -pedantic
            -pedantic-errors)
elseif (${CMAKE_CXX_COMPILER_ID} STREQUAL "MSVC")
    target_compile_options(sudoku PRIVATE
            /W3)
endif ()

# IDEs should put the headers in a nice place
source_group(TREE "${PROJECT_SOURCE_DIR}/include" PREFIX "Header Files" FILES ${HEADER_LIST})

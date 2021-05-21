set(CMAKE_CXX_FLAGS "-fprofile-instr-generate -fcoverage-mapping")
# Note that headers are optional, and do not affect add_library, but they will not
# show up in IDEs unless they are listed in add_library.

# set(HEADER_LIST "${ModernCMakeExample_SOURCE_DIR}/include/modern/lib.hpp")
# Optionally glob, but only for CMake 3.12 or later:
file(GLOB HEADER_LIST CONFIGURE_DEPENDS
        "${CppTicTacToe_SOURCE_DIR}/include/tictactoe/*.h"
        "${CppTicTacToe_SOURCE_DIR}/include/tictactoe/*.hpp")
file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
        "${CppTicTacToe_SOURCE_DIR}/src/*.cc"
        "${CppTicTacToe_SOURCE_DIR}/src/*.cpp")

# Make an automatic library - will be static or dynamic based on user setting
add_library(tictactoe ${SOURCE_LIST} ${HEADER_LIST})

# We need this directory, and users of our library will need it too
target_include_directories(tictactoe PUBLIC ../include)

# All users of this library will need at least C++11
target_compile_features(tictactoe PUBLIC cxx_std_11)

# IDEs should put the headers in a nice place
source_group(TREE "${PROJECT_SOURCE_DIR}/include" PREFIX "Header Files" FILES ${HEADER_LIST})

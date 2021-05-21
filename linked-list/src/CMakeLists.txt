# Note that headers are optional, and do not affect add_library, but they will not
# show up in IDEs unless they are listed in add_library.

# Optionally glob, but only for CMake 3.12 or later:
file(GLOB HEADER_LIST CONFIGURE_DEPENDS
        "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.h"
        "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.hpp")

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
        "${LinkedList_SOURCE_DIR}/src/*.h"
        "${LinkedList_SOURCE_DIR}/src/*.hpp"
        "${LinkedList_SOURCE_DIR}/src/*.cc"
        "${LinkedList_SOURCE_DIR}/src/*.cpp")

# This is a header-only library, so there is no compilation here.
add_library(cs126linkedlist INTERFACE)

# Users of our library will need this directory.
target_include_directories(cs126linkedlist INTERFACE "${LinkedList_SOURCE_DIR}/include/")

# IDEs should put the headers in a nice place.
source_group(TREE "${PROJECT_SOURCE_DIR}/include/" PREFIX "Header Files" FILES ${HEADER_LIST})

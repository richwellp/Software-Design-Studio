set(DOXYGEN_EXTRACT_ALL YES)
set(DOXYGEN_BUILTIN_STL_SUPPORT YES)

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
    "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.h"
    "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.hpp"
    "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.cc"
    "${LinkedList_SOURCE_DIR}/include/cs126linkedlist/*.cpp")

doxygen_add_docs(docs
    ${SOURCE_LIST}
    "${CMAKE_CURRENT_SOURCE_DIR}/README.md"
    WORKING_DIRECTORY
    "${PROJECT_SOURCE_DIR}/include"
)

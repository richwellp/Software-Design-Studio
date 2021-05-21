set(DOXYGEN_EXTRACT_ALL YES)
set(DOXYGEN_BUILTIN_STL_SUPPORT YES)

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
    "${Snake_SOURCE_DIR}/include/snake/*.h"
    "${Snake_SOURCE_DIR}/include/snake/*.hpp"
    "${Snake_SOURCE_DIR}/include/snake/*.cc"
    "${Snake_SOURCE_DIR}/include/snake/*.cpp")

doxygen_add_docs(docs
    ${SOURCE_LIST}
    "${CMAKE_CURRENT_SOURCE_DIR}/README.md"
    WORKING_DIRECTORY
    "${PROJECT_SOURCE_DIR}/include"
)

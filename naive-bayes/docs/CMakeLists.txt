set(DOXYGEN_EXTRACT_ALL YES)
set(DOXYGEN_BUILTIN_STL_SUPPORT YES)

file(GLOB SOURCE_LIST CONFIGURE_DEPENDS
    "${NaiveBayes_SOURCE_DIR}/include/bayes/*.h"
    "${NaiveBayes_SOURCE_DIR}/include/bayes/*.hpp"
    "${NaiveBayes_SOURCE_DIR}/include/bayes/*.cc"
    "${NaiveBayes_SOURCE_DIR}/include/bayes/*.cpp")

doxygen_add_docs(docs
    ${SOURCE_LIST}
    "${CMAKE_CURRENT_SOURCE_DIR}/README.md"
    WORKING_DIRECTORY
    "${PROJECT_SOURCE_DIR}/include"
)

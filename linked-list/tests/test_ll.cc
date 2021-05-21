// Copyright (c) 2020 [Your Name]. All rights reserved.

#define CATCH_CONFIG_MAIN

#include <cs126linkedlist/ll.h>

#include <catch2/catch.hpp>

using cs126linkedlist::LinkedList;

// Read more on SECTIONs here:
// `https://github.com/catchorg/Catch2/tree/master/docs`
// in the "Test Cases and Sections" file.
TEST_CASE("Push Back", "[constructor][push_back][size][empty][back][front]") {
  LinkedList<int> list;

  REQUIRE(list.size() == 0);
  REQUIRE(list.empty());

  SECTION("Push back one element") {
    list.push_back(1);
    REQUIRE(list.size() == 1);
  }

  SECTION("Push back two elements") {
    list.push_back(-1);
    list.push_back(10000);
    REQUIRE(list.size() == 2);
    REQUIRE(list.back() == 10000);
    REQUIRE(list.front() == -1);
  }
}

TEST_CASE("Constructors", "[copy][move][operators]") {
  LinkedList<int> list;
  list.push_back(0);
  list.push_back(1);

  SECTION("Vector constructor") {
    std::vector<int> values;
    values.push_back(0);
    values.push_back(1);
    LinkedList<int> list_from_vector(values);
    REQUIRE(list_from_vector.size() == 2);
    REQUIRE(list_from_vector.front() == 0);
    REQUIRE(list_from_vector.back() == 1);
  }
  SECTION("Copy constructor") {
    const LinkedList<int>& list_copy(list);
    REQUIRE(list_copy.size() == 2);
    REQUIRE(list_copy.back() == 1);
    REQUIRE(list_copy.front() == 0);
  }
  SECTION("Copy constructor operator") {
    LinkedList<int> list_copy;
    list_copy = list;
    REQUIRE(list_copy.size() == 2);
    REQUIRE(list_copy.back() == 1);
    REQUIRE(list_copy.front() == 0);
  }
  SECTION("Move constructor") {
    LinkedList<int> list_move(std::move(list));
    REQUIRE(list_move.front() == 0);
    REQUIRE(list_move.back() == 1);
    REQUIRE(list.empty());
  }
  SECTION("Move constructor operator") {
    LinkedList<int> list_move;
    list_move = std::move(list);
    REQUIRE(list_move.front() == 0);
    REQUIRE(list_move.back() == 1);
    REQUIRE(list.empty());
  }
}

TEST_CASE("Overloaded Operators", "[equals][not-equals]") {
  LinkedList<int> list;
  list.push_back(0);
  list.push_back(1);
  SECTION("Equals") {
    LinkedList<int> list2;
    list2.push_back(0);
    list2.push_back(1);
    REQUIRE(list == list2);
  }
  SECTION("Not Equals") {
    LinkedList<int> list3;
    list3.push_back(-1);
    list3.push_back(1);
    REQUIRE(list != list3);
  }
}

TEST_CASE("Container methods",
          "[push_front][pop_front][pop_back][clear][RemoveNth][RemoveOdd]") {
  SECTION("Push front") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);
    REQUIRE(list.front() == 1);
  }
  SECTION("Pop front") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);
    list.pop_front();
    REQUIRE(list.front() == 0);
  }
  SECTION("Pop back") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);
    list.pop_back();
    REQUIRE(list.back() == 1);
  }
  SECTION("Clear list") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);
    list.clear();
    REQUIRE(list.empty());
  }
  SECTION("RemoveNth") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);
    list.push_front(2);
    list.RemoveNth(1);
    REQUIRE(list.size() == 2);
  }
  SECTION("Remove Odd indexes") {
    LinkedList<int> list;
    list.push_front(0);
    list.push_front(1);  // must be removed
    list.push_front(2);
    list.push_front(3);  // must be removed
    list.RemoveOdd();
    REQUIRE(list.size() == 2);
    REQUIRE(list.front() == 2);
    REQUIRE(list.back() == 0);
  }
}

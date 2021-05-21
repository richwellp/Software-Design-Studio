// Copyright (c) 2020 CS126SP20. All rights reserved.

#include "ll.h"

#include <cassert>
#include <utility>
#include <vector>


namespace cs126linkedlist {

template <typename ElementType>
LinkedList<ElementType>::LinkedList() {
  head = nullptr;
  current_size = 0;
}

template <typename ElementType>
LinkedList<ElementType>::LinkedList(const std::vector<ElementType>& values) {
  if (values.size() == 0) {
    head = nullptr;
  } else {
    head = new Node;
    Node* current = head;
    for (ElementType value : values) {
      current->data = value;
      current = current->next;
    }
  }
  current_size = values.size();
}

// Copy constructor
template <typename ElementType>
LinkedList<ElementType>::LinkedList(const LinkedList<ElementType>& source) {
  if (source.size() == 0) {
    head = nullptr;
  } else {
    head = new Node;
    Node* current = head;
    Node* temp = source.head;
    while (temp != nullptr) {
      current->next = new Node();
      current->data = temp->data;
      current->next = temp->next;
      current = current->next;
      temp = temp->next;
    }
  }
  current_size = source.current_size;
}

// Move constructor
template <typename ElementType>
LinkedList<ElementType>::LinkedList(LinkedList<ElementType>&& source) noexcept {
  if (source.size() == 0) {
    head = nullptr;
  } else {
    head = new Node;
    Node* current = head;
    Node* temp = source.head;
    while (temp != nullptr) {
      current->next = new Node;
      current = current->next;
      current->data = temp->data;
      current->next = temp->next;
      temp = temp->next;
      temp->next = nullptr;
    }
  }
  current_size = source.current_size;
  source.current_size = 0;
}

// Destructor
template <typename ElementType>
LinkedList<ElementType>::~LinkedList() {
  Node* current = head;
  Node* next;
  while (current != nullptr) {
    next = current->next;
    delete current;
    current = next;
  }
}

// Copy assignment operator
template <typename ElementType>
LinkedList<ElementType>& LinkedList<ElementType>::operator=(
    const LinkedList<ElementType>& source) {
  if (&source == this) {
    return *this;
  }
  // free memory
  Node* current = head;
  Node* next;
  while (current != nullptr) {
    next = current->next;
    delete current;
    current = next;
  }
  // copy the source
  if (source.head == nullptr) {
    head = nullptr;
  } else {
    head = new Node;
    current = head;
    Node* temp = source.head;
    while (temp->next != nullptr) {
      current->data = temp->data;
      current->next = temp->next;
      current->next = new Node;
      current = current->next;
      temp = temp->next;
    }
  }
  current_size = source.current_size;
  return *this;
}

// Move assignment operator
template <typename ElementType>
LinkedList<ElementType>& LinkedList<ElementType>::operator=(
    LinkedList<ElementType>&& source) noexcept {
  if (&source == this) {
    return *this;
  }
  // free memory
  Node* current = head;
  Node* next;
  while (current != nullptr) {
    next = current->next;
    delete current;
    current = next;
  }
  // transfer ownership
  if (source.head == nullptr) {
    head = nullptr;
  } else {
    head = new Node;
    current = head;
    Node* temp = source.head;
    while (temp->next != nullptr) {
      current->data = temp->data;
      current->next = temp->next;
      current->next = new Node;
      current = current->next;
      temp = temp->next;
    }
  }
  current_size = source.current_size;
  source.~LinkedList<ElementType>();
  source.head = nullptr;
  source.current_size = 0;
  return *this;
}

template <typename ElementType>
void LinkedList<ElementType>::push_front(const ElementType& value) {
  Node* to_add = new Node;
  to_add->data = value;
  if (head == nullptr) {
    head = to_add;
  } else {
    Node* current = head;
    head = to_add;
    to_add->next = current;
  }
  current_size++;
}

template <typename ElementType>
void LinkedList<ElementType>::push_back(const ElementType& value) {
  Node* to_add = new Node;
  to_add->data = value;
  if (head == nullptr) {
    head = to_add;
  } else {
    Node* current = head;
    // assign current to the last/tail node of the list
    for (size_t i = 0; i < current_size - 1; i++) {
      current = current->next;
    }
    // to_add will be assigned to the tail->next
    current->next = to_add;
  }
  current_size++;
}

template <typename ElementType>
ElementType LinkedList<ElementType>::front() const {
  return head->data;
}

template <typename ElementType>
ElementType LinkedList<ElementType>::back() const {
  Node* current = head;
  for (size_t i = 0; i < current_size - 1; i++) {
    current = current->next;
  }
  return current->data;
}

template <typename ElementType>
void LinkedList<ElementType>::pop_front() {
  if (head != nullptr) {
    Node* to_delete = head;
    head = head->next;
    delete to_delete;
    current_size--;
  }
}

template <typename ElementType>
void LinkedList<ElementType>::pop_back() {
  if (head != nullptr) {
    Node* to_delete = head;
    // assign to_delete to the last/tail node of the list
    while (to_delete->next->next != nullptr) {
      to_delete = to_delete->next;
    }
    delete to_delete;
    current_size--;
  }
}

template <typename ElementType>
int LinkedList<ElementType>::size() const {
  return current_size;
}

template <typename ElementType>
bool LinkedList<ElementType>::empty() const {
  return current_size == 0;
}

template <typename ElementType>
void LinkedList<ElementType>::clear() {
  Node* current = head;
  Node* next;
  while (current != nullptr) {
    next = current->next;
    delete current;
    current = next;
  }
  head = nullptr;
  current_size = 0;
}

template <typename ElementType>
std::ostream& operator<<(std::ostream& os,
                         const LinkedList<ElementType>& list) {
  // Node* current = list.head;
  for (size_t index = 0; index < list.size(); index++) {
    os << index << ": " << std::endl;
  }
  return os;
}

template <typename ElementType>
void LinkedList<ElementType>::RemoveNth(int n) {
  if (n < 0 || n >= current_size) {
    return;
  } else if (n == 0) {
    pop_front();
  } else if (n == current_size - 1) {
    pop_back();
  } else {
    Node* to_delete = head;
    Node* prev = to_delete;  // previous node of the to_delete node
    // set to_delete to the Nth node in the list
    for (size_t i = 0; i < n - 1; ++i) {
      prev = to_delete;
      to_delete = to_delete->next;
    }
    // link prev to the 2nd next node
    prev->next = to_delete->next;
    delete to_delete;
    current_size--;
  }
}

template <typename ElementType>
void LinkedList<ElementType>::RemoveOdd() {
  int index_to_remove = 1;
  while (index_to_remove < current_size) {
    RemoveNth(index_to_remove);
    index_to_remove++;  // former odd indexes (should be removed) moved to even
  }
}

template <typename ElementType>
bool LinkedList<ElementType>::operator==(
    const LinkedList<ElementType>& rhs) const {
  Node* to_compare = rhs.head;
  for (Node* current = head; current != nullptr; current = current->next) {
    if (to_compare->data != current->data) {
      return false;
    }
    to_compare = to_compare->next;
  }
  return true;
}

template <typename ElementType>
bool operator!=(const LinkedList<ElementType>& lhs,
                const LinkedList<ElementType>& rhs) {
  return !(lhs == rhs);
}
// iterators
template <typename ElementType>
typename LinkedList<ElementType>::iterator& LinkedList<ElementType>::iterator::
operator++() {
  current_ = current_->next;
  iterator iter(current_);
  return iter;
}

template <typename ElementType>
ElementType& LinkedList<ElementType>::iterator::operator*() const {
  iterator iter(current_);
  return iter;
}

template <typename ElementType>
bool LinkedList<ElementType>::iterator::operator!=(
    const LinkedList<ElementType>::iterator& other) const {
  return current_->data != other.current_->data;
}

template <typename ElementType>
typename LinkedList<ElementType>::iterator LinkedList<ElementType>::begin() {
  iterator iter(head);
  return iter;
}

template <typename ElementType>
typename LinkedList<ElementType>::iterator LinkedList<ElementType>::end() {
  Node* current = head;
  // assign current to one past the last/tail node of the list
  for (size_t i = 0; i < current_size; i++) {
    current = current->next;
  }
  iterator iter(current);
  return iter;
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator&
LinkedList<ElementType>::const_iterator::operator++() {
  current_ = current_->next;
  const_iterator iter(current_);
  return iter;
}

template <typename ElementType>
const ElementType& LinkedList<ElementType>::const_iterator::operator*() const {
  const_iterator iter(current_);
  return iter;
}

template <typename ElementType>
bool LinkedList<ElementType>::const_iterator::operator!=(
    const LinkedList<ElementType>::const_iterator& other) const {
  return current_->data != other.current_->data;
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator
LinkedList<ElementType>::begin() const {
  const_iterator iter(head);
  return iter;
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator LinkedList<ElementType>::end()
    const {
  Node* current = head;
  // assign current to the one past the last/tail node of the list
  for (size_t i = 0; i < current_size; i++) {
    current = current->next;
  }
  const_iterator iter(current);
  return iter;
}

}  // namespace cs126linkedlist

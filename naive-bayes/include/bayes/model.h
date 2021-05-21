// Copyright (c) 2020 [Your Name]. All rights reserved.

#ifndef BAYES_MODEL_H_
#define BAYES_MODEL_H_

#include <cstdlib>
#include <ostream>

#include "image.h"

namespace bayes {

/*
 * We've given you a starter struct to represent the model.
 * You are totally allowed to delete, change, move, rename, etc. this struct
 * however you like! In fact, we encourage it! It only exists as a starting
 * point of reference.
 *
 * In our probabilities array we have a final dimension [2], which represents
 * the individual probabilities that a pixel for a class is either shaded or
 * not shaded. Since the probability that a pixel is shaded is just
 * (1 - probability not shaded), we COULD have deleted that final dimension
 * (and you can do so if you want to), but we left it in so that you could
 * see how the model would need to change if we were to keep track of the
 * probability that a pixel is white vs. gray vs. dark gray vs. black.
 *
 * You can delete this comment once you're done with it.
 */

// 0-9 inclusive.
constexpr size_t kNumClasses = 10;
// Shaded or not shaded.
constexpr size_t kNumShades = 2;
// the k-value for Laplace smoothing factor (0.1 - 10)
constexpr double kSmoothingFactor = 1.0;

/**
 * Represents a Naive Bayes classification model for determining the
 * likelihood that an individual pixel for an individual class is
 * white or black.
 */
class Model {
 public:
  /** returns a probability in the probs_ 4D array. */
  double& GetProbability(int& row, int& col, int& class_number, int& shade);
  /** assigns a probability data in a specific indexes of probs_ */
  void SetProbability(int& row, int& col, int& class_number, int& shade,
                      double& prob);
  /** returns the probability in prior given the index */
  double& GetPrior(int& class_number);
  /** assigns a value in priors_ */
  void SetPriors(int& class_number, double& prob);
  /** assigns a value in posterior_probability */
  double& GetPosteriorProbability(int& class_number);
  void SetPosteriorProbability(int& class_number, double& prob);
  /** calculates the probabilities of F(i,j) = f given class = c */
  void CalculateProbabilities();
  int& GetTrainingCount();
  /** sets a value for the training_count_ integer */
  void SetTrainingCount(int& count);
  /** updates the occurrences 4D array given that the image is correctly
   * configured. */
  void UpdateOccurrences(int& class_number, Image& image);
  /** adds a count in the class_count array given the class */
  void AddClassCount(int& class_number);
  /** creates a data model (.json file) based on the probs_ array */
  void GenerateJSON();
  /** Prints out the posterior probability table of the Naive Bayes classifier.
   */
  friend std::ostream& operator<<(std::ostream& os, const Model& model);

 private:
  // The individual probabilities for each pixel for each class for
  // whether it's shaded or not.
  //
  // Examples:
  // probs[2][10][7][0] is the computed probability that a pixel at
  // [2][10] for class 7 is not shaded.
  //
  // probs[0][0][0][1] is the computed probability that a pixel at
  // [0][0] for class 0 is shaded.
  double probs_[kImageSize][kImageSize][kNumClasses][kNumShades]{};
  // Keeps count on the number of times F(i,j) = f when class = c
  int occurrences_[kImageSize][kImageSize][kNumClasses][kNumShades] = {0};
  // Stores the number of training examples where class = c
  int class_count_[kNumClasses]{};
  // The priors P(class = c)
  double priors_[kNumClasses]{};
  // The number of training examples in the file
  int training_count_{};
  // The calculated posterior probability for each class
  double posterior_probability_[kNumClasses] = {0};
};
std::ostream& operator<<(std::ostream& os, const Model& model);
}  // namespace bayes

#endif  // BAYES_MODEL_H_

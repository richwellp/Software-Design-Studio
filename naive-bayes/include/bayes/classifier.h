// Copyright (c) 2020 [Your Name]. All rights reserved.

#ifndef BAYES_CLASSIFIER_H_
#define BAYES_CLASSIFIER_H_

#include <bayes/model.h>

#include <fstream>
#include <ostream>
#include <string>

namespace bayes {
class Classifier {
 public:
  Classifier();
  Classifier(std::string& images_path, std::string& labels_path);
  Classifier(std::string& images_path, std::string& labels_path,
             std::string& model_path);
  /** Trains given by the training images and training labels.
   * Generates a model based on the training data. */
  void Train();
  /** Classifies the given images. */
  void Classify();
  /** Modifies the probability_model's probs_ and priors_ according to the given
   * JSON file. Assuming file path is valid */
  void ReadJSON(std::string& model_path);
  /** Getter method for the probability model. */
  bayes::Model& GetProbabilityModel();

 private:
  std::string images;
  std::string labels;
  std::string model;
  bayes::Model probability_model;
};
}  // namespace bayes

#endif  // BAYES_CLASSIFIER_H_


// Copyright (c) 2020 [Your Name]. All rights reserved.

#include <bayes/classifier.h>
#include <bayes/image.h>
#include <bayes/model.h>
#include <gflags/gflags.h>

#include <cstdlib>
#include <iostream>
#include <string>

DEFINE_bool(train, false,
            "Whether it needs to compute a model from training images");
DEFINE_string(images, "data/testimages", "the 28 x 28 images file");
DEFINE_string(labels, "data/testlabels",
              "the label corresponding to the images file");
DEFINE_string(model, "data/model.json",
              "the model that from the training data");

int main(int argc, char** argv) {
  gflags::SetUsageMessage(
      "Classifying images using Naive Bayes. Pass --helpshort for options.");

  gflags::ParseCommandLineFlags(&argc, &argv, true);

  if (FLAGS_images.empty() || FLAGS_labels.empty()) {
    std::cerr << "Please provide a file path in the --images and --labels flag."
              << std::endl;
    return EXIT_FAILURE;
  }
  if (!FLAGS_train && FLAGS_model.empty()) {
    std::cerr << "Please provide a file path in the --model flag." << std::endl;
    return EXIT_FAILURE;
  }

  if (FLAGS_train) {
    bayes::Classifier classifier(FLAGS_images, FLAGS_labels);
    classifier.Train();
  } else {
    bayes::Classifier classifier(FLAGS_images, FLAGS_labels, FLAGS_model);
    classifier.Classify();
  }
  return EXIT_SUCCESS;
}

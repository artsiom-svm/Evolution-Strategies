#pragma once
#include "local_includes.h"
#include "es.h"
#include <jni.h>

class es_example
{
	//size fo population
	static constexpr size_t npop = 50;
	//noise standard devivation
	static constexpr double sigma = 0.1;
	//learing rate
	static constexpr double alpha = 0.001;

	//the truth solution to the problem
	std::vector<double> solution;
	//current best guess
	std::vector<double> tries;

	//normal random N(0,1)
	std::default_random_engine generator;
	std::normal_distribution<double> distribution;

	//java environment variables
	JavaVM *jvm;                      // Pointer to the JVM (Java Virtual Machine)
	JNIEnv *env;                      // Pointer to native interface


	double mean(const std::vector<double> set) const;
	double std(const std::vector<double> set) const;
	void runVM();
public:
	double es_example::f(const std::vector<double> guess) const;

	double mean_square_error(const std::vector<double> guess) const;
	es_example(const std::vector<double> solution);
	es_example(const size_t size);
	void evolve();
	std::vector<double> getSolution() const;
	std::vector<double> getApproximation() const;

	void output_data(const std::string filename, const size_t index) const;
	~es_example();
};


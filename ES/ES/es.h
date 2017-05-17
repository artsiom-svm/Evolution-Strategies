#pragma once
#include "local_includes.h"

namespace es
{
	class es
	{
	private:
		//current solution
		double* current_guess;
		//the size of the input data set
		const size_t data_size;

		double mean(const double* data) const;
		double std(const double* data) const;

	protected:
		//size fo population
		static constexpr size_t npop = 50;
		//noise standard devivation
		static constexpr double sigma = 0.1;
		//learing rate
		static constexpr double alpha = 0.001;
		
		//normal random N(0,1)
		std::default_random_engine generator;
		std::normal_distribution<double> distribution;


	public:
		double f(const double* guess) const;
		void evolve();
		double* get_current_guess() const;

		es(const size_t size);
		~es();
	};
}


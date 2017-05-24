#include "es.h"


double es::es::mean(const double * data) const
{
	double result = 0;
	for (size_t i = 0; i < es::npop; i++)
	{
		result += data[i];
	}

	return result / es::npop;
}

double es::es::std(const double * data) const
{
	double mean = this->mean(data);

	double* dev = new double[es::npop];
	
	for (size_t i = 0; i < es::npop; i++)
		dev[i] = (data[i] - mean) * (data[i] - mean);

	double result = sqrt(this->mean(dev));
	
	delete[] dev;
	return result;
}

double es::es::f(const double * guess) const
{
	
	double solution[] = { 0.5, 0.2, -0.3 };

	double award = 0;
	for (int i = 0; i < 3; i++)
	{
		award -= (guess[i] * solution[i]) * (guess[i] * solution[i]);
	}
	return award;
}

void es::es::evolve()
{
	//create a sample set of tries
	neural_network::matrix N([&]() {return es::distribution(es::generator); }, es::npop, data_size);
	//create learining error-means
	neural_network::matrix R([]() {return 0; }, es::npop, 1);
	//normalized learning vector
	neural_network::matrix A(es::npop, 1);

	//iterate award for random generated deviation from current guess
	for (size_t i = 0; i < es::npop; i++)
	{
		//add noise deviation from random generated matrix
		auto row = neural_network::matrix(N[i], 1, data_size);
		auto w_try = current_guess + row*es::sigma;

		//calculate award for new guess
		R[i][0] = f((double*)w_try);
	}

	//normalizy learing vector
	auto mean = this->mean((double*)R);
	auto std = this->std((double*)R);

	for (size_t i = 0; i < es::npop; i++)
	{
		A[i][0] = (R[i][0] - mean) / std;
	}

	//update guess solution
	// dot = n_tries^T * A
	//dot product of noise and learning vector
	auto dot = N.transpose() * A;		
	//update of value
	current_guess += dot * (es::alpha / (es::sigma * es::npop));

	
	
}

double * es::es::get_current_guess() const
{
	return (double*)current_guess;
}

es::es::es(const size_t size) : data_size(size), distribution(std::normal_distribution<double>(0, 1)), current_guess(1, size)
{
	if (size <= 0)
		throw std::logic_error("ERROR: Trying to create model with non-positive size of data set\n");

	for (size_t i = 0; i < data_size; i++)
		current_guess[0][i] = distribution(generator);
}

es::es::~es()
{
}

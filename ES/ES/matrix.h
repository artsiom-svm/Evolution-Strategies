#pragma once
#include "local_includes.h"
namespace neural_network
{
	class matrix
	{
		const size_t height;
		const size_t width;
		double** data;
	public:
		matrix();
		matrix(const size_t height, const size_t width);
		double* operator[](const size_t index);
		const double* operator[](const size_t index) const;
		matrix operator*(const matrix& right) const;
		matrix operator*(const double right) const;
		matrix operator+(const matrix& right) const;
		matrix operator-(const matrix& right) const;
		matrix hadamand(const matrix& right);
		matrix transpose();
	
		matrix for_each(std::function<double (double)> p);
		static matrix rand_init(const size_t height, const size_t width, const int seed);
		~matrix();
	};
}



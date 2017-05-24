#pragma once
#include "local_includes.h"
namespace neural_network
{
	class matrix
	{
		const size_t height;
		const size_t width;
		double** data;
		
		matrix* for_each(const std::function<double (double)> p);
	public:
		//deep copy constructor
		matrix(const matrix& copy);

		//empply matrix
		matrix();

		//initialization with all zero value
		matrix(const size_t height, const size_t width);

		//initialization from given 2d array
		matrix(const double** data, const size_t height, const size_t width);

		//initialization from given 1d array fisrt fill the rows
		matrix(const double* data, const size_t height, const size_t width);
		
		//initialize matrix with specif user function
		matrix(const std::function<double()> p, const size_t height, const size_t width);

		//native binary operation
		matrix operator*(const matrix& right) const;
		matrix operator*(const double right) const;
		matrix operator+(const matrix& right) const;
		matrix operator-(const matrix& right) const;
		matrix& operator+=(const matrix& right);
		//biraly direct product
		matrix hadamand(const matrix& right);

		//native matrix's transpose
		matrix transpose();

		//only explicit conversion
		//return matrix's data representation in one array row with size height*width
		explicit operator double*() const;

		//getters
		const size_t get_height() const;
		const size_t get_width() const;

		//data getter & setter
		double* operator[](const size_t index);
		const double* operator[](const size_t index) const;

		//pure random initialization [0..1]
		static matrix rand_init(const size_t height, const size_t width, const int seed);

		//debug matrix output
		friend std::ostream& operator<<(std::ostream& os, const matrix& m);

		~matrix();
	};
}



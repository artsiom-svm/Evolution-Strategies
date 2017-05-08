#include "local_includes.h"

#include <iostream>
#include <ctime>
#include "matrix.h"

int main()
{
	try {
		auto k = neural_network::matrix::rand_init(100, 100, 1);
		auto m = neural_network::matrix::rand_init(100, 100, 5);
		long t = std::clock();
		k*m;
		std::cout << -(t - std::clock())/(double)CLOCKS_PER_SEC;
		//std::cout << "done";
	}
	catch (std::exception e) {
		std::cerr << e.what();
	}
	
	system("pause");
	return 0;
}
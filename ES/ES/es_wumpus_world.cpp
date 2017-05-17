#include "es_wumpus_world.h"


double es_wumpus_world::mean_square_error(const void * solution, const void * w)
{
	double result = 0;
	int sol = *(int*)solution;
	int* data = (int*)w;

#ifndef SIZE_OF_BATCH
	throw std::logic_error("in es_wumpus_world::mean_square_gradient the size of batch is not specified");
#else
	for (int i = 0; i < SIZE_OF_BATCH; i++)
	{
		result += (data[i] - sol) * (data[i] - sol);
	}
#endif //SIZE_OF_BATCH

	return result;
}

es_wumpus_world::es_wumpus_world()
{

}


es_wumpus_world::~es_wumpus_world()
{
}

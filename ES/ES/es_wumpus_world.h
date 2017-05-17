#pragma once
#include "es.h"

class es_wumpus_world
{
public:
	double mean_square_error(const void* solution, const void* w);
	es_wumpus_world();
	~es_wumpus_world();
};


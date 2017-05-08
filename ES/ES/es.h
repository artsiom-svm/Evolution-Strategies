#pragma once
#include "local_includes.h"

class es
{
public:
	double mean_square_error(const void* solution, const void* w);
	es();
	~es();
};


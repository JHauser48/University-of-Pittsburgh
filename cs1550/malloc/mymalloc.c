#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>

#include "mymalloc.h"

// Don't change or remove these constants.
#define MINIMUM_ALLOCATION  16
#define SIZE_MULTIPLE       8

typedef struct Block
{
	unsigned int data_size;
	bool in_use;
	struct Block* next;
	struct Block* prev;
	
} Block;

Block *head = NULL;
Block *tail = NULL;

unsigned int round_up_size(unsigned int data_size)
{
	if(data_size == 0)
		return 0;
	else if(data_size < MINIMUM_ALLOCATION)
		return MINIMUM_ALLOCATION;
	else
		return (data_size + (SIZE_MULTIPLE - 1)) & ~(SIZE_MULTIPLE - 1);
}

Block* create_head_block(unsigned int size)
{
	Block* head_block = (Block*)sbrk(size + sizeof(Block));
	head_block->data_size = size;
	head_block->in_use = true;
	head = head_block;
	tail = head_block;
	head_block->next = tail;
	head_block->prev = tail; 
	return head_block;
}

Block* new_block(unsigned int size)
{	
	Block* new_block;
	new_block = (Block*)sbrk(size + sizeof(Block));
	new_block->data_size = size;
	new_block->in_use = true;
	new_block->prev = tail;
	tail->next = new_block;
	tail = new_block;
	new_block->next = head;
	head->prev = tail;
	return new_block;
}

Block* find_best_fit(unsigned int size)
{
	int best_fit = -1;
	int head_check = 0;
	int temp;
	Block* block;
	Block* best_block;

	for(block = head; block != NULL; block = block->next)
	{
		if(block == tail->next && head_check == 1){
			if(best_fit == -1){
				return NULL;
			}
			block = NULL;
			best_block->in_use = true;
			return best_block;
		}

		head_check = 1;

		if(best_fit < 0 && block->in_use == false){
			temp = block->data_size - size;
			if(temp >= 0){
				best_fit = temp;
				best_block = block;
			}
		}else if(block->in_use == false){
			temp = block->data_size - size;
			if(temp >= 0 && temp < best_fit)
			{
				best_fit = temp;
				best_block = block;
			}
		}
	}

	return best_block;
}

void contract_tail(Block* freed_block)
{
	if(freed_block == tail && freed_block == head){
		tail = NULL;
		head = NULL;
	}else{
		tail = freed_block->prev;
		tail->next = head;
		head->prev = tail;
	}

	int dealloc_size = -1 * (sizeof(Block) + freed_block->data_size);
	sbrk(dealloc_size);
}

Block *check_next(Block *freed_block)
{
	Block *next_block = freed_block->next;

	if(next_block != head && next_block->in_use == false){
		freed_block->data_size += (next_block->data_size + sizeof(Block));
		if(tail == next_block){
			tail = freed_block;
		}
		freed_block->next = next_block->next;
		next_block->next->prev = freed_block;

	}

	return freed_block;
}

Block* check_prev(Block *freed_block)
{
	Block *prev_block = freed_block->prev;

	if(prev_block != tail && prev_block->in_use == false){
		prev_block->data_size += (freed_block->data_size + sizeof(Block));

		if(freed_block == tail){
			Block* temp = freed_block->next;
			temp->prev = prev_block;
			prev_block->next = freed_block->next;
			tail = prev_block;
			return prev_block;
			
		}else{
			Block* temp = freed_block->next;
			temp->prev = prev_block;
			prev_block->next = freed_block->next;
			return prev_block;
		}
	}else{
		if(freed_block == tail && prev_block->in_use == false){
			prev_block->next = freed_block->next;
			tail = prev_block;
			return prev_block;
		}
		return freed_block;
	}
}

void split_block(Block* malloc_block, int size)
{
	int new_block_size = malloc_block->data_size - size - 16;
	malloc_block->data_size = size;

	char *split_block_char = (((char*)malloc_block->next) - new_block_size - sizeof(Block));
	Block* split_block = (Block*)split_block_char;

	malloc_block->next->prev = split_block;
	split_block->next = malloc_block->next;
	malloc_block->next = split_block;
	split_block->prev = malloc_block;
	split_block->in_use = false;
	split_block->data_size = new_block_size;

	if(malloc_block == tail){
		tail = split_block;
		head = split_block->next;
	}
}

void* my_malloc(unsigned int size)
{
	if(size == 0)
		return NULL;

	size = round_up_size(size);

	Block *block;

	if(head == NULL){
		block = create_head_block(size);
	}else{
		block = find_best_fit(size);
	}

	if (block == NULL){
		block = new_block(size);
	}else{

		int min_block_bytes = 32;
		if((block->data_size - size) >= min_block_bytes){
			split_block(block, size);	
		}	
	}

	return (void*)((block) + 1);
}

void my_free(void* ptr)
{
	if(ptr == NULL)
		return;

	Block* freed_block = (Block*)ptr - 1;

	freed_block->in_use = false;
	freed_block = check_next(freed_block);
	freed_block = check_prev(freed_block);

	if(freed_block == tail){
		contract_tail(freed_block);

	}
}
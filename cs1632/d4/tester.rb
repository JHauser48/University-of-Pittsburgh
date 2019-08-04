#tester
require_relative 'graph_reader'
require_relative 'path_finder'
require_relative 'word_find'

graph = GraphReader.new('ultra_big_graph.txt')
graph.create_graph()
graph.print_graph()

finder = PathFinder.new(graph)
finder.find_all_paths()

finder.print_paths()
graph.print_graph()

word_find = WordFind.new(finder.all_paths)
word_find.permute_paths()
#word_find.check_dictionary()

#print "Longest: "
#print word_find.find_longest_word()
require 'flamegraph'
require_relative 'word_find2'
require_relative 'graph_reader'
require_relative 'path_finder'

Flamegraph.generate('flamegrapherOpt.html') do
  reader = GraphReader.new('big_graph.txt')
  reader.create_graph()
  finder = PathFinder.new(reader)
  finder.find_all_paths()
  word_find = WordFind2.new(finder.all_paths)
  word_find.permute_paths()
  #word_find.check_dictionary
  word_find.find_longest_word()
end

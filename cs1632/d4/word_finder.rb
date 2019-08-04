require_relative 'word_find'
require_relative 'graph_reader'
require_relative 'path_finder'
# main program which runs the word_finder
exit if ARGV.empty? || ARGV.length > 1
filename = ARGV[0]
reader = GraphReader.new(filename)
exit unless reader.create_graph != -1
finder = PathFinder.new(reader)
finder.find_all_paths
word_find = WordFind.new(finder.all_paths)
word_find.build_trie
word_find.permute_paths
word_find.find_longest_word

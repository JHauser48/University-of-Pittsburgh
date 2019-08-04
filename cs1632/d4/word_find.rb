require_relative 'path_finder'
require 'rambling-trie'

# class to permute all graph paths and check dictionary for valid words
class WordFind
  def initialize(paths)
    @paths = paths
    @path_permutations = []
    @dictionary = 'wordlist.txt'
    @valid_words = []
    @max_words = []
    @trie = Rambling::Trie.create
    @vowels = %w[A E I O U]
  end

  def build_trie
    File.foreach(@dictionary) do |line|
      @trie.add line.chomp
    end
    @trie.word?('test')
  end

  def permute_paths
    @paths.each do |y|
      valid = check_grammar_rules(y)
      perms = y.chars.permutation.map(&:join) unless valid == false
      next if perms.nil?

      perms.each do |x|
        search_dict(x)
      end
    end
  end

  def check_grammar_rules(string)
    return false if string.include?('Q') && !string.include?('U')

    vowel_count = get_vowel_count(string)
    return false if vowel_count.zero?

    validlong = check_long_string(vowel_count, string) if string.length >= 8
    return false if validlong == false

    true
  end

  def get_vowel_count(string)
    vowel_count = 0
    @vowels.each do |x|
      string.chars.each do |y|
        vowel_count += 1 if y == x
      end
    end
    vowel_count
  end

  def check_long_string(vowel_count, string)
    return false if vowel_count <= (string.length / 2.2)
    return false if vowel_count >= (string.length * 0.65)
  end

  def search_dict(string)
    @valid_words.push(string) if @trie.word? string.downcase
  end

  def find_longest_word
    longest = @valid_words.max_by(&:length).length
    @valid_words.each do |x|
      @max_words.push(x) if x.length == longest && !@max_words.include?(x)
    end
    puts 'Longest valid word(s):'
    @max_words.each { |x| puts x }
  end
end

import sqlite3 as lite
import csv
import re
con = lite.connect('cs1656.sqlite')

with con:
    cur = con.cursor() 

	########################################################################		
	### CREATE TABLES ######################################################
	########################################################################		
	# DO NOT MODIFY - START 
    cur.execute('DROP TABLE IF EXISTS Actors')
    cur.execute("CREATE TABLE Actors(aid INT, fname TEXT, lname TEXT, gender CHAR(6), PRIMARY KEY(aid))")

    cur.execute('DROP TABLE IF EXISTS Movies')
    cur.execute("CREATE TABLE Movies(mid INT, title TEXT, year INT, rank REAL, PRIMARY KEY(mid))")

    cur.execute('DROP TABLE IF EXISTS Directors')
    cur.execute("CREATE TABLE Directors(did INT, fname TEXT, lname TEXT, PRIMARY KEY(did))")

    cur.execute('DROP TABLE IF EXISTS Cast')
    cur.execute("CREATE TABLE Cast(aid INT, mid INT, role TEXT)")

    cur.execute('DROP TABLE IF EXISTS Movie_Director')
    cur.execute("CREATE TABLE Movie_Director(did INT, mid INT)")
	# DO NOT MODIFY - END

	########################################################################		
	### READ DATA FROM FILES ###############################################
	########################################################################		
	# actors.csv, cast.csv, directors.csv, movie_dir.csv, movies.csv
	# UPDATE THIS

    with open('actors.csv', 'r') as file:
      reader = csv.reader(file)
      actor_list = list(reader)
      file.close()

    with open('cast.csv', 'r') as file:
      reader = csv.reader(file)
      cast_list = list(reader)
      file.close()

    with open('directors.csv', 'r') as file:
      reader = csv.reader(file)
      director_list = list(reader)
      file.close()

    with open('movie_dir.csv', 'r') as file:
      reader = csv.reader(file)
      movie_dir_list = list(reader)
      file.close()

    with open('movies.csv', 'r') as file:
      reader = csv.reader(file)
      movies_list = list(reader)
      file.close()

	########################################################################		
	### INSERT DATA INTO DATABASE ##########################################
	########################################################################		
	# UPDATE THIS TO WORK WITH DATA READ IN FROM CSV FILES
    for actor in actor_list:
    	cur.execute('''INSERT INTO Actors
    	               VALUES(?,?,?,?)''', (actor[0], actor[1], actor[2], actor[3]))
  
    for movie in movies_list:
        cur.execute('''INSERT INTO Movies
    	               VALUES(?,?,?,?)''', (movie[0], movie[1], movie[2], movie[3]))

    for cast_mem in cast_list:
    	cur.execute('''INSERT INTO Cast
    	               VALUES(?,?,?)''', (cast_mem[0], cast_mem[1], cast_mem[2]))

    for director in director_list:
    	cur.execute('''INSERT INTO Directors
    	               VALUES(?,?,?)''', (director[0], director[1], director[2]))

    for movie_dir in movie_dir_list:
    	cur.execute('''INSERT INTO Movie_Director
    	               VALUES(?,?)''', (movie_dir[0], movie_dir[1]))

    con.commit()
    
	########################################################################		
	### QUERY SECTION ######################################################
	########################################################################		
  
    queries = {}

	# DO NOT MODIFY - START 	
	# DEBUG: all_movies ########################
    queries['all_movies'] = '''
SELECT * FROM Movies
'''	
	# DEBUG: all_actors ########################
    queries['all_actors'] = '''
SELECT * FROM Actors
'''	
	# DEBUG: all_cast ########################
    queries['all_cast'] = '''
SELECT * FROM Cast
'''	
	# DEBUG: all_directors ########################
    queries['all_directors'] = '''
SELECT * FROM Directors
'''	
	# DEBUG: all_movie_dir ########################
    queries['all_movie_dir'] = '''
SELECT * FROM Movie_Director
'''	
	# DO NOT MODIFY - END

	########################################################################		
	### INSERT YOUR QUERIES HERE ###########################################
	########################################################################		
	# NOTE: You are allowed to also include other queries here (e.g., 
	# for creating views), that will be executed in alphabetical order.
	# We will grade your program based on the output files q01.csv, 
	# q02.csv, ..., q12.csv

    ################################################################
    ########################## VIEWS ###############################
    ################################################################
    cur.execute('DROP VIEW IF EXISTS all80_to_90_actors')
    cur.execute('DROP VIEW IF EXISTS all2000_actors')
    cur.execute('DROP VIEW IF EXISTS rouge_one')
    cur.execute('DROP VIEW IF EXISTS star_wars_films')
    cur.execute('DROP VIEW IF EXISTS star_wars_cast')
    cur.execute('DROP VIEW IF EXISTS post_1985_actors')
    cur.execute('DROP VIEW IF EXISTS top_10_cast')
    cur.execute('DROP VIEW IF EXISTS female_cast_count')
    cur.execute('DROP VIEW IF EXISTS male_cast_count')
    cur.execute('DROP VIEW IF EXISTS debut_year_films')
    cur.execute('DROP VIEW IF EXISTS kevin_bacon')
    cur.execute('DROP VIEW IF EXISTS bacon_films')
    cur.execute('DROP VIEW IF EXISTS bacon_costars')
    cur.execute('DROP VIEW IF EXISTS bacon_costar_films')

    queries['a1_view'] = '''
    CREATE VIEW all80_to_90_actors AS
    SELECT a.aid
    FROM Actors as a, Cast as c, Movies as m
    WHERE a.aid = c.aid AND c.mid = m.mid AND m.year >= 1980 AND m.year <= 1990
    '''

    queries['a2_view'] = '''
    CREATE VIEW all2000_actors AS
    SELECT a.aid
    FROM Actors as a, Cast as c, Movies as m
    WHERE a.aid = c.aid AND c.mid = m.mid AND m.year >= 2000
    '''

    queries['a3_view'] = '''
    CREATE VIEW rouge_one AS
    SELECT year as year, rank as rank
    FROM Movies as m
    WHERE title = 'Rouge One: A Star Wars Story'
    '''

    queries['a4_view'] = '''
    CREATE VIEW star_wars_films AS
    SELECT mid
    FROM Movies 
    WHERE title LIKE '%Star Wars%'
    '''

    queries['a5_view'] = '''
    CREATE VIEW star_wars_cast AS
    SELECT DISTINCT a.aid, m.title 
    FROM Actors as a, Cast as c, Movies as m
    WHERE a.aid = c.aid AND c.mid in star_wars_films AND m.mid = c.mid
    '''

    queries['a6_view'] = '''
    CREATE VIEW post_1985_actors AS
    SELECT a.aid
    FROM Actors as a, Cast as c, Movies as m
    WHERE a.aid = c.aid AND c.mid = m.mid AND m.year >= 1985
    '''

    queries['a7_view'] = '''
    CREATE VIEW top_10_cast AS
    SELECT count(*) as cast_num
    FROM Movies as m, Cast as c
    WHERE m.mid = c.mid
    GROUP BY m.mid
    ORDER BY cast_num DESC
    LIMIT 10
    '''

    queries['a8_view'] = '''
    CREATE VIEW female_cast_count AS
    SELECT m.mid, count(*) as f_cast_num
    FROM Movies as m, cast as c, Actors as a
    WHERE m.mid = c.mid AND c.aid = a.aid AND a.gender = "Female"
    GROUP BY m.mid
    '''


    queries['a9_view'] = '''
    CREATE VIEW male_cast_count AS
    SELECT m.mid, count(*) as m_cast_num
    FROM Movies as m, cast as c, Actors as a
    WHERE m.mid = c.mid AND c.aid = a.aid AND a.gender = "Male"
    GROUP BY m.mid
    '''

    queries['aa1_view'] = '''
    CREATE VIEW debut_year_films AS 
    SELECT DISTINCT a.aid, min(year) as debut
    FROM Actors as a, Cast as c, Movies as m
    WHERE a.aid = c.aid AND c.mid = m.mid 
    GROUP BY a.aid
    '''

    queries['aa2_view'] = '''
    CREATE VIEW kevin_bacon AS
    SELECT a.aid as aid
    FROM Actors as a
    Where a.fname = "Kevin" AND a.lname = "Bacon"
    '''

    queries['aa3_view'] = '''
    CREATE VIEW bacon_films AS
    SELECT m.mid
    FROM Movies as m, Cast as c, kevin_bacon as k
    WHERE m.mid = c.mid AND c.aid = k.aid
    '''

    queries['aa4_view'] = '''
    CREATE VIEW bacon_costars AS
    SELECT DISTINCT a.aid
    FROM Actors as a, Cast as c, Movies as m, kevin_bacon as k
    WHERE c.aid = a.aid AND c.mid = m.mid AND m.mid in bacon_films 
          AND a.aid != k.aid 
    '''

    queries['aa5_view'] = '''
    CREATE VIEW bacon_costar_films AS
    SELECT DISTINCT m.mid as mid
    FROM Movies as m, bacon_costars as bc, bacon_films as bf, Cast as c
    WHERE c.aid = bc.aid AND c.mid = m.mid AND c.mid NOT IN bacon_films
    '''

    #################################################################
    ######################## END VIEWS ##############################
    #################################################################

	# Q01 ########################		
    queries['q01'] = '''
    SELECT lname, fname
    FROM Actors as a
    WHERE a.aid in all80_to_90_actors AND a.aid in all2000_actors
    ORDER BY lname ASC, fname ASC
    '''	
	
	# Q02 ########################		
    queries['q02'] = '''
    SELECT title, m.year
    FROM movies as m, rouge_one as r
    WHERE m.year = r.year AND m.rank > r.rank
    ORDER BY title ASC
    '''	

	# Q03 ########################		
    queries['q03'] = '''
    SELECT DISTINCT fname, lname, count(*) as num_films
    FROM Actors as a, star_wars_cast as c
    WHERE a.aid =  c.aid
    GROUP BY a.aid
    ORDER BY num_films DESC, lname ASC, fname ASC
    '''	

	# Q04 ########################		
    queries['q04'] = '''
    SELECT lname, fname
    FROM Actors as a, Cast as c
    WHERE NOT a.aid IN post_1985_actors AND a.aid = c.aid
    ORDER BY lname ASC, fname ASC
    '''	

	# Q05 ########################		
    queries['q05'] = '''
    SELECT fname, lname, count(*) as films_directed
    FROM Directors as d, Movie_Director as m
    WHERE d.did = m.did
    GROUP BY d.did
    ORDER BY films_directed DESC, lname ASC, fname ASC
    LIMIT 20
    '''	

	# Q06 ########################		
    queries['q06'] = '''
    SELECT title, count(*) as cast_number
    FROM Movies as m, Cast as c
    WHERE m.mid = c.mid
    GROUP BY m.mid
    HAVING cast_number >= (SELECT min(t.cast_num) FROM top_10_cast as t)
    ORDER BY cast_number DESC
    '''	

	# Q07 ########################		
    queries['q07'] = '''
    SELECT m.title, f.f_cast_num, g.m_cast_num
    FROM Movies as m, female_cast_count as f, male_cast_count as g
    WHERE m.mid = f.mid AND m.mid = g.mid AND f.f_cast_num > g.m_cast_num
    GROUP BY m.title
    ORDER BY m.title ASC
    '''	

	# Q08 ########################		
    queries['q08'] = '''
    SELECT a.fname, a.lname, count(DISTINCT d.did) as num_directors
    FROM Actors as a, Cast as c, Directors as d, Movie_Director as md
    WHERE a.aid = c.aid AND c.mid = md.mid AND md.did = d.did 
          AND (a.fname != d.fname AND a.lname != d.lname)
    GROUP BY a.aid
    HAVING num_directors >= 7
    ORDER BY num_directors DESC
    '''	

	# Q09 ########################		
    queries['q09'] = '''
    SELECT fname, lname, count(*) as debut_films, d.debut
    FROM Actors as a, cast as c, Movies as m, debut_year_films as d
    WHERE a.fname LIKE 'S%' AND a.aid = c.aid AND 
          c.mid = m.mid AND m.year = d.debut AND a.aid = d.aid
    GROUP BY a.aid
    ORDER BY debut_films DESC
    '''	

	# Q10 ########################		
    queries['q10'] = '''
    SELECT d.lname, m.title
    FROM Actors as a, Cast as c, Movies as m, Directors as d, Movie_Director as md
    WHERE a.aid = c.aid AND m.mid = md.mid AND md.mid = c.mid AND d.did = md.did  
          AND (a.lname = d.lname AND a.fname != d.fname)
    ORDER BY d.lname ASC
    '''	

	# Q11 ########################		
    queries['q11'] = '''
    SELECT DISTINCT a.fname, a.lname
    FROM Actors as a, Cast as c, kevin_bacon as k
    WHERE a.aid = c.aid AND c.mid IN bacon_costar_films
          AND a.aid != k.aid AND c.aid NOT IN bacon_costars
    ORDER BY a.lname ASC, a.fname ASC
    '''	

	# Q12 ########################		
    queries['q12'] = '''
    SELECT fname, lname, count(*) as num_movies, avg(m.rank) as popularity
    FROM Actors as a, Cast as c, Movies as m 
    WHERE a.aid = c.aid AND m.mid = c.mid
    GROUP BY a.aid
    ORDER BY popularity DESC
    LIMIT 20
    '''	

	########################################################################		
	### SAVE RESULTS TO FILES ##############################################
	########################################################################		
	# DO NOT MODIFY - START 	
    for (qkey, qstring) in sorted(queries.items()):
        try:
            cur.execute(qstring)
            all_rows = cur.fetchall()
			
            print ("=========== ",qkey," QUERY ======================")
            print (qstring)
            print ("----------- ",qkey," RESULTS --------------------")
            for row in all_rows:
            	print (row)
            print (" ")

            save_to_file = (re.search(r'q0\d', qkey) or re.search(r'q1[012]', qkey))
            if (save_to_file):
            	with open(qkey+'.csv', 'w') as f:
            		writer = csv.writer(f)
            		writer.writerows(all_rows)
            		f.close()
            	print ("----------- ",qkey+".csv"," *SAVED* ----------------\n")
		
        except lite.Error as e:
            print ("An error occurred:", e.args[0])
	# DO NOT MODIFY - END
	

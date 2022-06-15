# PageRank

This project needs to read a large file whose size is 56MiB compressed or 245 MiB uncompressed. My laptop does not contain uncompressed tools. Therefore, I read the file in a compressed state by GZIPInputStream. Also, the file contains over a million sources and target links. If one did not finish the read and store the data in linear time, the program would seem like run an infinite loop.

After reading the compressed file, the program will start to do the PageRank algorithm process. To handle more than a million strings from Map<string double>, I would design the procedure complete in linear time. Otherwise, if run in O(n^2), it means the program has to spend a ton of time on it. For example, in the do-while loop, for the Q's size is zero, the program has to update all the pages in P for R.

The differences between the pages with the highest inlink count and those with the highest PageRank.

The highest inlink count is an effective approach to measuring the popularity of web pages.

The program will count the number of inlinks for each page, then use these data to provide a ranking list. PageRank is an algorithm based on link analysis. It uses the idea of the random surfer to calculate corresponds to finding what is known as the stationary probability distribution of a random walk on the graph of the Web.

Compare to the highest inlink count and highest PageRank, the PageRank can provide a more reliable rating of web pages because the link analysis algorithms are allowed between states that are all equally probable and are given by the links. The highest inlink count is susceptible to spam.

Its result may not approach human expectations. For example, inlinks.txt, 2007, 2008, 2006 have a higher inlink frequency than the Biography. In PageRank.txt, Biography has a higher rank than 2007, 2008, 2006. Maybe Biography has the transition that are allowed between states are all equally probable and are given by the links.

# DC Gallery Photo Download

## About
Downloads all "recommended post" photos from (Red Velvet) Seulgi's DC Gallery page. It can be tweaked, however, to download photos from any of your favorite celebrities with a DC gallery page (see below *Updates* for major and minor gallery differences).

## Beware
Depending on how many posts the gallery page has, this program may save _**literally**_ millions of photos onto your computer and take several days to complete. Read *How to* to avoid this deluge.

## How to
1. Create a temporary file where all photos will be downloaded, and add that file to the directory address around line 271.
> OLD: /Users/(Mac Computer Username)/Desktop/ <br/>
> NEW: /Users/(Mac Computer Username)/Desktop/(File Name)/
2. Create a `temp.txt` file in the location you'd like to download with the following information:
> Jan 06, 2019 (09:30:01 AM) <-- Date of last download <br/>
> 336703 <-- Earliest post you want to have downloaded (1, if you'd like to download *everything*)
3. Depending on the most recent post number, you may want to increase the above value to avoid having too many photos downloaded. You can check the post number for different dates.
4. If you are downloading photos of Red Velvet's Seulgi, then great—run the program!
5. If you are downloading photos of another celebrity, look through the code for single-line comments and replace all instance of id=seulgi with the id= of your choice and run!

## Background Dev Story
As a Korean, I've understood that K-Pop is a global phenomenon, but it had never impacted my life until I joined the military (*other than in 2013, when everyone asked whether I knew "Gangnam Style"*).

My interest in this particular celebrity has led me to develop a program where I could periodically update a repostiory of photos with the click of a button, without undergoing the menial task of individually downloading every photo from recommended posts.

## Updates
To download from **m.gallery** pages, simply replace all instances of the URL as shown below. (Below URL's will not work—replace id= with celebrity of your choice.)

> Major gallery: https://gall.dcinside.com/board/lists/?id=(celebrity) <br/>
> m.gallery (Minor gallery): https://gall.dcinside.com/mgallery/board/lists/?id=(celebrity)

### IP Block
Due to the high amount of traffic this program causes, DC Gallery has blocked access to their website for anyone who tries to make a high volume of requests in a short period of time, which is essentially what this program does as it goes through the list of posts to select "recommended posts" and opens each post to download all photos attached.

*A getaround for this (if you have the patience) is to alter thread sleep time around lines 173 and 220.*
> t1.sleep(553); <-- 0.553s delay between each download <br/>
> t1.sleep(1000); <-- 1s delay between each download

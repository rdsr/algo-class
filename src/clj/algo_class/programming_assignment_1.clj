(ns algo-class.programming-assigment-1
  (:use [clojure.java.io :as io]))

;; The file contains all the 100,000 integers between 1 and 100,000
;; (including both) in some random order( no integer is repeated).

;; Your task is to find the number of inversions in the file given
;; (every row has a single integer between 1 and 100,000). Assume your
;; array is from 1 to 100,000 and ith row of the file gives you the
;; ith entry of the array.  Write a program and run over the file
;; given. The numeric answer should be written in the space.  So if
;; your answer is 1198233847, then just type 1198233847 in the space
;; provided without any space / commas / any other punctuation
;; marks. You can make upto 5 attempts, and we'll count the best one
;; for grading.  (We do not require you to submit your code, so feel
;; free to choose the programming language of your choice, just type
;; the numeric answer in the following space

(defn- merge-subroutine [l-sorted r-sorted]
  (loop [l l-sorted r r-sorted acc []]
    (let [a (first l)
          b (first r)]
      (cond
       (nil? a) (concat acc r)
       (nil? b) (concat acc l)
       (> a b) (recur l (rest r) (conj acc b))
       :else (recur (rest l) r (conj acc a))))))

(defn- count-subroutine
  [l-sorted r-sorted]
  (loop [l l-sorted r r-sorted add (count l-sorted) cnt 0]
    (let [a (first l)
          b (first r)]
      (cond
       (nil? a) cnt
       (nil? b) cnt
       (> a b) (recur l (rest r) add (+ cnt add))
       :else (recur (rest l) r (dec add) cnt)))))

(defn- count-and-merge
  [l-sorted r-sorted]
  [(merge-subroutine l-sorted r-sorted) (count-subroutine l-sorted r-sorted)])

(defn- count-inversions
  [nos]
  (let [sz (count nos)
        m (/ sz 2)]
    (if (<= sz 1)
      [nos 0]
      (let [[l r] (split-at m nos)
            [l-sorted l-cnt] (count-inversions l)
            [r-sorted r-cnt] (count-inversions r)
            [merged cnt] (count-and-merge l-sorted r-sorted)]
        [merged (+ l-cnt r-cnt cnt)]))))

(defn solve []
  (with-open [rdr (io/reader "resources/programming_assignment1/IntegerArray.txt")]
    (second (count-inversions (map #(Integer/parseInt %) (line-seq rdr))))))

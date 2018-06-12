# Chatbot

An n-gram Chatbot

In this question you will implement a chatbot by generating random sentences from your HW1 corpus using
n-gram language models.
We have created a vocabulary file vocabulary.txt for you to interpret the data, though you do not need it
for programming. The vocabulary is created by tokenizing the corpus, converting everything to lower case,
and keeping word types that appears three times or more. There are 4699 lines in the vocabulary.txt file.
Download the corpus WARC201709 wid.txt from the homework website (note: this is a processed and
different file than the corpus we used in earlier HW). This file has one word token per line, and we have
already converted the word to its index (line number) in vocabulary.txt. Thus you will see word indices from
1 to 4699. In addition, we have a special word type OOV (out of vocabulary) which has index 0. All word
tokens that are not in the vocabulary map to OOV. For example, the rst OOV in the corpus appears as
392 are
1512 entirely
0 undermined
12 .
The words on the right are provided from the original essays for readability, they are not in the corpus. The
word \undermined" is not in the vocabulary, therefore it is mapped to OOV and have an index 0. OOV
represents a set of out-of-vocabulary words such as \undermined, extra-developed, metro, customizable,
optimizable" etc. But for this homework you can treat OOV as a single special word type. Therefore, the
vocabulary has v = 4700 word types. The corpus has 228548 tokens.
Write a program Chatbot.java with the following command line format, where the commandline input
has variable length1 and the numbers are integers:
$java Chatbot FLAG number1 [number2 number3 number4]

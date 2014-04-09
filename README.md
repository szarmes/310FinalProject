COSC310 Final Project Duncan Szarmes
=========

This is the repository for Assignment 3 in COSC 310. The assignment is to create a simple chat agent.  
We chose to have our agent, codebot, function as a teaching assistant or tutor for first year computer 
science.

FEATURES (A2)

Currently, codebot can be greeted by the user and when prompted for a topic will provide a basic 
definition of what the topic is. For example, if asked what variables are codebot will tell the user 
Variables have a type. This type can be a base type, such as int or double, or an object.  Variables
also need to have an identifier such as "number1".

FEATURES (A3)

-Succesfully used sockets to have a conversation with Ethan Owusu's codebot (demonstrated in video)

-Integrated basic spell-check, which helped improve the conversations flow by catching spelling mistakes and guessing (usually correctly) what the user was actually trying to spell
-Made a simple GUI
-Loads of topics codebot can respond to
-Gives over 5 different responses when it doesn't know how to correctly respond

Spell check sample output:

You: tell me about arrais

Codebot: An array is a container object that holds a fixed number of values of a single type. The length of an array is established when the array is created. After creation, its length is fixed. What other questions do you have about this topic? 
You: what about the syze of arrais?

Codebot: The size of an array is the number of elements in CAN contain. To find the size use the array.size attribute.  The largest index available in the array is one less than the size. Anything else regarding this topic? 


NEW FEATURES (Final Project)

Wolfram

-Makes use of the Wolfram Alpha API to return information when asking questions about famous people. For instance, if a user asks "Who is Bill Gates?" the program will display information it retrieves from wolfram about to Bill Gates (what he's famous for, birth information, physical information, etc.). Also using the Wolfram Alpha API, Codebot can now solve a wide range of mathematical questions. Sample questions are "solve (31-3)+sin(2)", "solve 3+x where x=3", or even "derivative 3x+2sin(x)" etc..

Sample of feature (finding a derivative):

How else may I be of service? 
You: x^2 + 2x^e^x d/dx
Codebot: Here is what Wolfram has to say on the matter: 

Derivative d/dx(3 x^3+2 x) = 9 x^2+2

Alternate form (sqrt(2)-3 i x) (sqrt(2)+3 i x)
Complex roots x = -(i sqrt(2))/3
Complex roots x = (i sqrt(2))/3
Roots in the complex plane 
Properties as a real function R  (all real numbers)
Properties as a real function {y element R : y>=2}
Properties as a real function even
Indefinite integral  integral (2+9 x^2) dx = 3 x^3+2 x+constant
Global minimum min{(d(3 x^3+2 x))/(dx)} = 2 at x = 0

Do you need help with anything else? 



Twitter

-Makes use of the Twitter4J API to pull tweets about specific topics that are unknown to codebot. Before, it would only prompt to perform a google search, but now half of the time it will ask if you want some tweets pulled instead.

Sample of feature:

Codebot: Anything else I can assist you with? 
You: tell me about obama
Codebot: I have no knowledge on this matter. Some tweets about this might be of help to you, wanna see some? 
You: sure
Codebot: Here ya go! 

@roxyCOCKer - @DopeKittyy_ tell me about it. Obama here. 😁

@Alfredo_Al_Nava - Don't let the Obama Administration cover up the truth about drone strikes. 
Tell your representative to cosponsor... http://t.co/JIIz3quRig

@kasilane - Learn about our government and how it works. Then you can tell me about how much you think Obama sucks.

@lazdamar - Lots of debate re Hersch article, note he writes about what others tell him; seems to me his article shows divide re RTE in Obama admin

@MysteryOnward - @MartyLeft You tell me. It seems like you know more about something than I do. What might #Obama have on #Roberts?

@bhabikaj - Obama is on campus, and I am learning about rocks in Geology. TELL ME WHAT IS WRONG WITH THAT STATEMENT.

How else may I be of service? 


Translate

-Makes use of the GoogleTranslate API to allow the conversation to be held in either English, French, or Spanish. The language you want to converse in can be selected using the radio buttons in the GUI. However, if either Spanish or French are selected, the features that use the Twitter4J and WolframAlpha API's won't work properly. 

Sample of feature:

Codebot:  What's up? 
You: Bonjour!
Codebot: Avez-vous besoin d'aide
You: oui
Codebot: Que puis-je vous aider
You: ce sont tableau?
Codebot: Un tableau est un objet conteneur qui contient un nombre fixe de valeurs d'un seul type La longueur d'un tableau est établi lorsque le tableau est créé après la création de sa longueur est fixée Autre chose concernant ce sujet



JavaDocs for spellchecking, searching through text files, and google searching can be found in the docs folder of the github repo:

https://github.com/szarmes/310FinalProject



-------------------------

How to run:

Download the repo as a ZIP

Import the project into Eclipse

To run the Codebot, open RunCodebot.java and click run

To run the socket implementation, first open Server.java and run, then open Client.java and run 
(In ClientBot, change the IP address in the socket connection to "localhost" if running both ClientBot and ServerBot on a single computer, otherwise change the IP address to the IP of the computer running Serverbot, usually located in Network Preferences)

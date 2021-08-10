# web
My library application where you can see free books that library has and order books for you to read it as reader.As librarian you can manage users orders and
apporve and delete users's orders when users make orders. Also you can add new books as an librarian to store new books. As admin you can also add new books and 
besides manage users and librarians that means that you can promote reader role to librarian and demote librarian role to reader and set subscription to users and 
librarians and admins. Subscriptions allows user to order book and read it at once if order date matches with subscription dates range if user has subscription. If 
user has not subscription or order date doesn't matches with user subscription that user has to wait for order approvement from librarian or admin.

Application uses MySQL database and uses connection pool and Data access object pattern and dao layer and sevrice layer and commands layer with java tomcat servlets.
There are some desing patterns implemented in project (abstract factory, strategy, singleton, proxy).
Nearly 170 tests was written that testst dao layer and service layer. Also dao layer and service layer are logged with the help of log4j2.
Project uses bootstrap css framework that allows to customise java server pages (jsp).
On client side I use ajax for asynchronus requests. That my first experiense so I think it was not bad use of ajax.

Application localiszed on sveral foreign languages those are russian, english, indian, chinese and arabic languages.

I'm glad to that I was able to make connection pool that check weather it's time to grow or trim connection pool in time with Timer and TimerTask classes those are 
executes in some period in time. Also inner connection pool structure consisits of blocking queue alllows me not to think of pool synchronization that blocking queue 
does itself. Aloso i've made singleton classes with the help of nested classes that allows me not to synchronize getInstance() methods but just make nested class that 
encapsulates single class instance in nested class and retrieve it inside of getInstance() method. Also it's very interesting to that I used ajax technology that makes
asynchtonus browser client requests and allows me not to make redirects but only make forward and simplify my command pattern. I make pages localization based on what 
language user chose when starts application. I've made client side and server side validation too.

I have some thought about application future another ideas for application which I think to make if I have enogugh free time.

I've made a lot of work and i tryed to be my best.

# reporter
simple report web-application

So once i was sitting at the job and discussing our reports that used Birt engine. I don't like it,
so i wanted to create something that will show how reports should look like from my perspective and try something new.
For me reports are just some presentation of sql results. 
So idea is:
- We jave filesystem simple db( Here i implemented a really simple db, based on json files,
so db can be changed in any text editor)
- This db contains sql queries and params for them.
- Application take it, build form based on params, show it to user. 
- User enter needed params and passto the server.
- Application execute queries with passed params and return result to the user.

There to ways to deal with frontend here. 
1. Some static html is build into the server
2. There is standalone frontend on vuejs and tailwindcss that uses rest calls to the server.

Thing I wanted to try with this project:

1. quarkus framework
2. accessing db othen than usual for me Spring Data, Hibernate and Srping jdbc template (here i use apache commons library)
3. mustache templates
4. vuejs
5. tailwindcss

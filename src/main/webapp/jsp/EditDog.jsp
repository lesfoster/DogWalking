<HTML>
<HEAD>
<TITLE>Modify Dog Info</TITLE>
</HEAD>
<BODY>

<%@ page import="main.*" %>
<%@ page import="servlet.*" %>

<CENTER>
<H3>Modify Dog Info:</H3>

<FORM action="/Dogs/Action?OP=EditDogProc" method="post">


<% 		
	Dog dog = (Dog) request.getAttribute("Dog");


		out.println(dog.outputEditHTML());
%>
<P><P>

<INPUT TYPE="HIDDEN" NAME="El" VALUE="<%out.print(dog.getId());%>">


<INPUT TYPE="SUBMIT" NAME="UPDATE" VALUE="APPLY CHANGES">
<P>

<INPUT TYPE="SUBMIT" NAME="CANCEL" VALUE="CANCEL CHANGES">

<BR>

</FORM>

</CENTER>
</BODY>
</HTML>


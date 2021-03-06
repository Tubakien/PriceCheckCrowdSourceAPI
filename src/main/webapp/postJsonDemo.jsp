<%--
  Created by IntelliJ IDEA.
  User: student
  Date: 4/2/17
  Time: 6:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create New Item</title>
</head>
<body>
<div>
    <h4>New User JSON Format</h4>
    <form method="post" action="http://localhost:8080/pricerequest/JSON/newuser">
    <%--<form method="post" action="http://52.14.136.34:8080/pricecheck/pricerequest/JSON/newuser">--%>
        <input type="submit" value="Add User">
    </form>

</div>
<div>
    <h3>Sample Store</h3>
    <table>
        <th>
        <td>Store Name</td>
        <td>Store Address</td>
        <td>Latitude</td>
        <td>Longtitude</td>
        </th>
        <tr>
            <td>Costco</td>
            <td>2850 Hoepker Rd, Sun Prairie, WI 53590</td>
            <td>43.168132</td>
            <td>-89.273714</td>
        </tr>
    </table>
</div>
<div>
    <h4>Add Item JSON Format</h4>
    <form method="post" action="http://localhost:8080/pricerequest/JSON/create">
    <%--<form method="post" action="http://52.14.136.34:8080/pricecheck/pricerequest/JSON/create">--%>
        Item name : <input type="text" placeholder="item to add" id="item2" name="item"> <br>
        Item price : <input type="number" step="0.01" min="0" placeholder="item price in dollars" id="itemPrice2" name="itemPrice"> <br>
        Item unit : <input type="text" placeholder="item unit e.g. oz." id="itemUnit2" name="itemUnit"> <br>
        Item unit value: <input type="number" step="0.01" min="0" placeholder="unit quantity" id="itemUnitValue2" name="itemUnitValue"> <br>
        Brand name : <input type="text" placeholder="item brand" id="brandName2" name="brandName"> <br>
        Store name : <input type="text" placeholder="store item is sold" id="storeName2" name="storeName"> <br>
        Store address : <input type="text" placeholder="store address" id="storeAddress2" name="storeAddress"> <br>
        Latitude : <input type="number"  step="any" placeholder="user location latitude" id="latitude2" name="latitude"> <br>
        Longtitude : <input type="number" step="any" placeholder="user location longtitude" id="longtitude2" name="longtitude"> <br>
        Api Key : <input type="text" placeholder="user API key" id="apiKey2" name="apiKey"> <br>
        <input type="submit" value="Submit">
    </form>

</div>
</body>
</html>

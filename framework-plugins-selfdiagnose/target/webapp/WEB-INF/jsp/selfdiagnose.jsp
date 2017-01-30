<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
	<head>
		<style>
			table {
				border-collapse: collapse;
				width: 100%;
			}

			th, td {
				text-align: left;
				padding: 8px;
			}

			tr:nth-child(even){background-color: #f2f2f2}

			th {
				background-color: #4CAF50;
				color: white;
			}
        </style>
	</head>
	<body>
		<table>
		    <tr>
		        <th style="width: 35%">Key</th>
		        <th>Value</th>
            </tr>
            <c:forEach items="${items}" var="item">
                <tr>
                    <td>${item.key}</td>
                    <td>${item.value}</td>
                </tr>
            </c:forEach>
		</table>
        <br/></br>
		Version: ${version}<br/>
        Time: ${time}
	</body>
</html>
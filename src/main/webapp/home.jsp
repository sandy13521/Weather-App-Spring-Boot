<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Weather Report</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.2/css/bulma.css" />
</head>

<body>
    <section class="hero is-primary">
        <div class="hero-body">
            <div class="container">
                <img src="w2.jpg" alt="Weather Is Bright" style="width: 70px;height: 70px;float: left;margin-right: 20px">
                <h1 class="title" style="padding-top: 10px">
                    Weather Report
                </h1>
            </div>
        </div>
    </section>
    <section class="section">
        <div class="container">
            <div class="columns">
                <div class="column is-offset-4 is-4">
                    <form method="POST" action="city">
                        <div class="field has-addons">
                            <div class="control is-expanded">
                                <input class="input" type="text" placeholder="City Name" name="city">
                            </div>
                            <div class="control">
                                <button class="button is-info">
                                    Add City
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <section class="section">
        <div class="container">
            <div class="columns">
                <div class="column is-offset-4 is-4">
                    <c:forEach items="${city}" var="city">
                        <div class="box">
                            <article class="media">
                                <div class="media-left">
                                    <figure class="image is-50x50">
                                        <img src="http://openweathermap.org/img/w/${city.icon}.png"
                                             alt="Image">
                                    </figure>
                                </div>
                                <div class="media-content">
                                    <div class="content">
                                        <p>
                                            <span class="title"> ${city.name}</span>
                                            <br>
                                            <span class="subtitle">${city.temp}° C</span>
                                            <br> ${city.des}
                                            <br> ${city.date}

                                        </p>
                                    </div>
                                </div>
                                <div class="media-right">
                                    <a href="/refresh/${city.name}">
                                        <img src="re.png" alt="Image"
                                             style="width:40px;height: 40px;margin-left:10px;margin-top: 10px">
                                    </a>
                                </div>
                                <div class="media-right">
                                    <a href="/delete/${city.name}">
                                        <img src="delete.png" alt="Image"
                                             style="margin-left:10px;width:50px;height: 40px;margin-top: 10px">
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
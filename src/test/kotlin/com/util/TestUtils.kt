package com.util

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun MockHttpServletRequestBuilder.addBasicAuthHeader(): MockHttpServletRequestBuilder =
    this.header("Authorization", "Basic dXNlcjpwYXNz")
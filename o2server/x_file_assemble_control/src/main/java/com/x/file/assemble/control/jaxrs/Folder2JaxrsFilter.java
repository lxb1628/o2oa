package com.x.file.assemble.control.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerUserJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/folder2/*",asyncSupported = true)
public class Folder2JaxrsFilter extends CipherManagerUserJaxrsFilter {

}

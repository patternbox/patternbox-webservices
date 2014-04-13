/**************************** Copyright notice ********************************

Copyright (C)2014 by D. Ehms, http://www.patternbox.com
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 ******************************************************************************/
package com.patternbox.webservices.application.library;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.patternbox.webservices.domain.model.Author;
import com.patternbox.webservices.domain.model.AuthorRepository;

/**
 * Library RESTful resources
 * 
 * @author <a href='http://www.patternbox.com'>D. Ehms, Patternbox</a>
 */
@Path("/library")
@Stateless
public class LibraryService {

	@Inject
	private Logger logger;

	@Inject
	private AuthorRepository authorRepository;

	@Inject
	private DataImporter dataImporter;

	@PostConstruct
	private void init() {
		try {
			dataImporter.importAuthors();
		} catch (IOException e) {
			throw new IllegalStateException("Data import failed.", e);
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String root() {
		logger.info("Method 'root' called.");
		return "Hello Library Service";
	}

	@GET
	@Path("/authors")
	@Produces(MediaType.APPLICATION_XML)
	public List<Author> authors() {
		logger.info("Method 'authors' called.");
		return authorRepository.all();
	}

	@GET
	@Path("/author/{email}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Author author(@PathParam("email") String email) {
		logger.info("Method 'author' called, email: " + email);
		return authorRepository.findByEmail(email);
	}
}

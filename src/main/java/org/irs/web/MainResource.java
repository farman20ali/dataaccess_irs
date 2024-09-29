package org.irs.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.irs.dto.RequestDto;
import org.irs.dto.ResponseDto;
import org.irs.service.UserService;

import io.quarkus.logging.Log;
@Path("/irs")
public class MainResource {

  @Inject
  UserService userService;
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/user")
  public ResponseDto userService(RequestDto requestDto) {
    try {
      Log.info("RPIN : "+ requestDto);
      return userService.fetchData(requestDto);

    }catch (Exception e){
      ResponseDto responseDto=new ResponseDto();
      responseDto.setFailure(e.getMessage());
      return  responseDto;
    }

  }

}

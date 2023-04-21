package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.orm.ServiceRequestData;

public interface IRequestController {

  ServiceRequestData sendRequest();

  void cancelRequest();
}

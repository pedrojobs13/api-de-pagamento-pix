package com.pedro.pagamento.api.exceptionHandler;

import com.pedro.pagamento.exception.BusinessException;
import com.pedro.pagamento.exception.EntidadeEmUsoException;
import com.pedro.pagamento.exception.EntidadeNaoEncontradaException;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
      "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, "
          + "entre em contato com o adiministrador do sistema";

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> tratarEntidadeNaoEncontradaException(
      EntidadeNaoEncontradaException ex, WebRequest request) {

    HttpStatus status = HttpStatus.NOT_FOUND;
    String details = ex.getMessage();
    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;

    Problem problem = createProblemBuilder(status, problemType, details).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> tratarBusinessException(
      EntidadeNaoEncontradaException ex, WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    ProblemType problemType = ProblemType.ERRO_DE_NEGOCIO;
    String details = ex.getMessage();

    Problem problem = createProblemBuilder(status, problemType, details).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> tratarEntidadeEmUsoException(
      EntidadeEmUsoException ex, WebRequest request) {
    HttpStatus status = HttpStatus.CONFLICT;
    String details = ex.getMessage();
    ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

    Problem problem = createProblemBuilder(status, problemType, details).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<?> tratarException(Exception ex, WebRequest request) {
//    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//    String details = MSG_ERRO_GENERICA_USUARIO_FINAL;
//    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
//
//    Problem problem = createProblemBuilder(status, problemType, details).build();
//
//    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
//  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {

    body = bodyValido(body, (HttpStatus) statusCode);
    return super.handleExceptionInternal(ex, body, headers, statusCode, request);
  }

  private Problem.ProblemBuilder createProblemBuilder(
      HttpStatus status, ProblemType problemType, String details) {
    return Problem.builder()
        .status(status.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(details);
  }

  private static Object bodyValido(Object body, HttpStatusCode status) {
    if (body == null) {
      body =
          Problem.builder()
              .title(Arrays.toString(status.toString().split("([0-9])\\w")))
              .status(status.value())
              .build();
    } else if (body instanceof String) {
      body = Problem.builder().title((String) body).status(status.value()).build();
    }
    return body;
  }
}

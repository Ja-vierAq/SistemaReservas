package reservas.logic;

// en resumen esta clase define los codigos para las operaciones q se van a hacer
// ejemplo: solicitar todas las categorias = codigo 306

public final class Protocol {

    private Protocol() {}

    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    //usuarios
    public static final int USUARIO_LOGIN = 101;
    public static final int USUARIO_READ = 102;
    public static final int USUARIO_CAMBIAR_CLAVE = 103;

    //funcionarios
    public static final int FUNCIONARIO_CREATE = 201;
    public static final int FUNCIONARIO_READ = 202;
    public static final int FUNCIONARIO_UPDATE = 203;
    public static final int FUNCIONARIO_DELETE = 204;
    public static final int FUNCIONARIO_SEARCH = 205;
    public static final int FUNCIONARIO_FIND_ALL = 206;

    //categorias
    public static final int CATEGORIA_CREATE = 301;
    public static final int CATEGORIA_READ = 302;
    public static final int CATEGORIA_UPDATE = 303;
    public static final int CATEGORIA_DELETE = 304;
    public static final int CATEGORIA_SEARCH = 305;
    public static final int CATEGORIA_FIND_ALL = 306;

    //recursos
    public static final int RECURSO_CREATE = 401;
    public static final int RECURSO_READ = 402;
    public static final int RECURSO_UPDATE = 403;
    public static final int RECURSO_DELETE = 404;
    public static final int RECURSO_SEARCH = 405;
    public static final int RECURSO_FIND_ALL = 406;

    //reservas
    public static final int RESERVA_CREATE = 501;
    public static final int RESERVA_READ = 502;
    public static final int RESERVA_CANCEL = 503;
    public static final int RESERVA_FIND_BY_FUNCIONARIO = 504;
    public static final int RESERVA_FIND_ALL = 505;

    //calendarios y actividades
    public static final int CALENDARIZACION_RECURSOS = 601;
    public static final int RESERVA_EN = 602;
    public static final int INICIO_SEMANA = 603;
    public static final int ACTIVIDADES_SEMANA = 604;
    public static final int ACTIVIDADES_EN = 605;

    //stats
    public static final int ESTADISTICAS_RECURSOS = 701;
    public static final int ESTADISTICAS_ACTIVIDADES = 702;

    //ia
    public static final int IA_EXTRAER = 801;
    public static final int IA_BUSCAR_CATEGORIA = 802;

    //notis asincronicas
    public static final int NOTIFICACION_RESERVA = 901;

    //respuestas
    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR = 1;

    // coneccion
    public static final int DISCONNECT = 99;
}

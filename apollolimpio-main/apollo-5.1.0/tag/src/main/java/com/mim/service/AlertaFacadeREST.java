/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.service;

import com.mim.entities.Alerta;
import com.mim.entities.Audiomensaje;
import com.mim.entities.Contacto;
import com.mim.entities.Paciente;
import com.mim.entities.Tipoalerta;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author robb
 */
@RequestScoped
@Path("com.mim.alerta")
public class AlertaFacadeREST extends AbstractFacade<Alerta> {

    @Inject
    @Push
    private PushContext channelName;
    @PersistenceContext(unitName = "hackaton2022")
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @jakarta.annotation.Resource
    private ManagedThreadFactory managedThreadFactory;

    public static final String ACCOUNT_SID = "ACfb27aad3716d80d53fc9c4aba50d3201";
    public static final String AUTH_TOKEN = "e40db4f5c395524b2060cffad0cf734e";

    public AlertaFacadeREST() {
        super(Alerta.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createAlert(Alerta entity) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        transaction.begin();
        entity.setFecha(new Date());
        if (entity.getLat().equals("0.0")) {//32.50882333719957, -116.98983089909702
            entity.setLat("32.50882333719957");
            entity.setLng("-116.98983089909702");
        }
        Random r = new Random();
        int low = 100000000;
        int high = 999999999;
        int result = r.nextInt(high - low) + low;
        entity.setCodigo("alert" + String.valueOf(result));
        super.create(entity);
        em.flush();
        transaction.commit();

        channelName.send("Holaaaaaaaaaaaa");

        if (entity.getPacienteIdpaciente() != null) {
            if (entity.getPacienteIdpaciente().getIdpaciente() != null) {
                if (entity.getTipoalertaIdtipoalerta() != null) {
                    if (entity.getTipoalertaIdtipoalerta().getIdtipoalerta() != null) {
                        TypedQuery<Paciente> queryPaciente = em.createQuery("SELECT c FROM Paciente c WHERE c.idpaciente = :id", Paciente.class);
                        queryPaciente.setParameter("id", entity.getPacienteIdpaciente().getIdpaciente());

                        Paciente pc = queryPaciente.getSingleResult();

                        if (entity.getTipoalertaIdtipoalerta().getIdtipoalerta() == 1) {
                            System.out.println("5");
                            sendSMSNotificationsHighAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), entity.getCodigo(), entity.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), entity.getPacienteIdpaciente().getIdpaciente(), entity.getTipoalertaIdtipoalerta().getIdtipoalerta(), entity.getLat(), entity.getLng());
                        } else {
                            System.out.println("6");
                            sendSMSNotificationsLOWAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), entity.getCodigo(), entity.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), entity.getPacienteIdpaciente().getIdpaciente(), entity.getTipoalertaIdtipoalerta().getIdtipoalerta(), entity.getLat(), entity.getLng());
                        }
                    } else {
                        System.out.println("4");
                    }
                } else {
                    System.out.println("3");
                }
            } else {
                System.out.println("2");
            }
        } else {
            System.out.println("1");
        }
        /*Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+526641632976"),
                "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                "dsdsds")
                .create();

        System.out.println(message.getSid());*/
        return String.valueOf(entity.getCodigo());
    }

    @POST
    @Path("switch/{paciente}/{state}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createIfNescesaryAlert(@PathParam("paciente") Integer id, @PathParam("state") Integer state) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        TypedQuery<Paciente> query = em.createQuery("SELECT c FROM Paciente c WHERE c.idpaciente = :id", Paciente.class);
        query.setParameter("id", id);

        Paciente paciente = query.getSingleResult();

        TypedQuery<Alerta> queryPrevious = em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.idpaciente = :idPaciente AND c.estatus != :est", Alerta.class);
        queryPrevious.setParameter("idPaciente", id);
        queryPrevious.setParameter("est", "Close");
        List<Alerta> alertaList = queryPrevious.getResultList();
        if (alertaList.isEmpty()) {
            transaction.begin();

            Alerta alerta = new Alerta();
            Random r = new Random();
            int low = 100000000;
            int high = 999999999;
            int result = r.nextInt(high - low) + low;
            alerta.setCodigo("alert" + String.valueOf(result));
            if (state.equals(1)) {
                alerta.setEstatus("Open");

                /* Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+526641632976"),
                        "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                        "Se creo alerta")
                        .create();
                System.out.println(message.getSid());*/
            } else {
                alerta.setEstatus("Close");
                /*Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+526641632976"),
                        "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                        "Se cerro la alerta")
                        .create();
                System.out.println(message.getSid());*/
            }
            alerta.setFecha(new Date());//32.50882333719957, -116.98983089909702
            alerta.setLat("32.50882333719957");
            alerta.setLng("-116.98983089909702");

            TypedQuery<Tipoalerta> queryTipoAlerta = em.createQuery("SELECT c FROM Tipoalerta c WHERE c.idtipoalerta = 1", Tipoalerta.class);
            alerta.setTipoalertaIdtipoalerta(queryTipoAlerta.getSingleResult());
            alerta.setPacienteIdpaciente(paciente);

            em.persist(alerta);
            em.flush();

            transaction.commit();
            if (alerta.getPacienteIdpaciente() != null) {
                if (alerta.getPacienteIdpaciente().getIdpaciente() != null) {
                    if (alerta.getTipoalertaIdtipoalerta() != null) {
                        if (alerta.getTipoalertaIdtipoalerta().getIdtipoalerta() != null) {
                            TypedQuery<Paciente> queryPaciente = em.createQuery("SELECT c FROM Paciente c WHERE c.idpaciente = :id", Paciente.class);
                            queryPaciente.setParameter("id", alerta.getPacienteIdpaciente().getIdpaciente());

                            Paciente pc = queryPaciente.getSingleResult();

                            if (alerta.getTipoalertaIdtipoalerta().getIdtipoalerta() == 1) {
                                System.out.println("5");
                                sendSMSNotificationsHighAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), alerta.getCodigo(), alerta.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alerta.getPacienteIdpaciente().getIdpaciente(), alerta.getTipoalertaIdtipoalerta().getIdtipoalerta(), alerta.getLat(), alerta.getLng());
                            } else {
                                System.out.println("6");
                                sendSMSNotificationsLOWAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), alerta.getCodigo(), alerta.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alerta.getPacienteIdpaciente().getIdpaciente(), alerta.getTipoalertaIdtipoalerta().getIdtipoalerta(), alerta.getLat(), alerta.getLng());
                            }
                        } else {
                            System.out.println("4");
                        }
                    } else {
                        System.out.println("3");
                    }
                } else {
                    System.out.println("2");
                }
            } else {
                System.out.println("1");
            }
        } else {
            transaction.begin();
            for (Alerta alerta : alertaList) {
                if (state.equals(1)) {
                    alerta.setEstatus("Open");
                     em.merge(alerta);

                } else {
                    alerta.setEstatus("Close");
                     em.merge(alerta);

                }
               
            }
            transaction.commit();

        }

        channelName.send("Holaaaaaaaaaaaa");

        return Response.ok().build();
    }

    @GET
    @Path("panic/{paciente}/{state}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createIfNescesaryPanicAlert(@PathParam("paciente") Integer id, @PathParam("state") Integer state) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        TypedQuery<Paciente> query = em.createQuery("SELECT c FROM Paciente c WHERE c.idpaciente = :id", Paciente.class);
        query.setParameter("id", id);

        Paciente paciente = query.getSingleResult();

        TypedQuery<Alerta> queryPrevious = em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.idpaciente = :idPaciente AND c.estatus != :est", Alerta.class);
        queryPrevious.setParameter("idPaciente", id);
        queryPrevious.setParameter("est", "Close");
        List<Alerta> alertaList = queryPrevious.getResultList();
        if (alertaList.isEmpty()) {
            transaction.begin();

            Alerta alerta = new Alerta();
            Random r = new Random();
            int low = 100000000;
            int high = 999999999;
            int result = r.nextInt(high - low) + low;
            alerta.setCodigo("alert" + String.valueOf(result));
            if (state.equals(1)) {
                alerta.setEstatus("Open");

                /* Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+526641632976"),
                        "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                        "Se creo alerta")
                        .create();
                System.out.println(message.getSid());*/
            } else {
                alerta.setEstatus("Close");
                /*Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+526641632976"),
                        "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                        "Se cerro la alerta")
                        .create();
                System.out.println(message.getSid());*/
            }
            alerta.setFecha(new Date());//32.50882333719957, -116.98983089909702
            alerta.setLat("32.50882333719957");
            alerta.setLng("-116.98983089909702");

            TypedQuery<Tipoalerta> queryTipoAlerta = em.createQuery("SELECT c FROM Tipoalerta c WHERE c.idtipoalerta = 1", Tipoalerta.class);
            alerta.setTipoalertaIdtipoalerta(queryTipoAlerta.getSingleResult());
            alerta.setPacienteIdpaciente(paciente);

            em.persist(alerta);
            em.flush();

            transaction.commit();
            if (alerta.getPacienteIdpaciente() != null) {
                if (alerta.getPacienteIdpaciente().getIdpaciente() != null) {
                    if (alerta.getTipoalertaIdtipoalerta() != null) {
                        if (alerta.getTipoalertaIdtipoalerta().getIdtipoalerta() != null) {
                            TypedQuery<Paciente> queryPaciente = em.createQuery("SELECT c FROM Paciente c WHERE c.idpaciente = :id", Paciente.class);
                            queryPaciente.setParameter("id", alerta.getPacienteIdpaciente().getIdpaciente());

                            Paciente pc = queryPaciente.getSingleResult();

                            if (alerta.getTipoalertaIdtipoalerta().getIdtipoalerta() == 1) {
                                System.out.println("5");
                                sendSMSNotificationsHighAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), alerta.getCodigo(), alerta.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alerta.getPacienteIdpaciente().getIdpaciente(), alerta.getTipoalertaIdtipoalerta().getIdtipoalerta(), alerta.getLat(), alerta.getLng());
                            } else {
                                System.out.println("6");
                                sendSMSNotificationsLOWAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), alerta.getCodigo(), alerta.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alerta.getPacienteIdpaciente().getIdpaciente(), alerta.getTipoalertaIdtipoalerta().getIdtipoalerta(), alerta.getLat(), alerta.getLng());
                            }
                        } else {
                            System.out.println("4");
                        }
                    } else {
                        System.out.println("3");
                    }
                } else {
                    System.out.println("2");
                }
            } else {
                System.out.println("1");
            }
        } else {
            transaction.begin();
            for (Alerta alerta : alertaList) {
                if (state.equals(1)) {
                    alerta.setEstatus("Open");
                    em.merge(alerta);

                } else {
                    alerta.setEstatus("Close");
                    em.merge(alerta);

                }

            }
            transaction.commit();

        }

        channelName.send("Holaaaaaaaaaaaa");

        return Response.ok().build();
    }

    @GET
    @Path("api/{name}")
    @Produces({"audio/ogg"})
    public Response getFile(@PathParam("name") String name) {
        if (name != null) {

            File file = new File("/opt/audios/" + name);

            if (file.exists()) {

                ResponseBuilder response = Response.ok(file);
                response.header("Content-Disposition", "attachment; filename=" + name);
                response.header("nombre", name);
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Credentials", "true");
                response.header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization");
                response.header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                return response.build();
            } else {
                return Response.noContent().build();
            }
        } else {
            return Response.noContent().build();
        }

    }

    @POST
    @Path("/prime")
    @Consumes({"multipart/form-data"})
    public void saveImagen2(MultipartFormDataInput multiPart) throws IOException {
        String nombre = null;
        InputStream stream = null;
        Map<String, List<InputPart>> list = multiPart.getFormDataMap();
        List<InputPart> inputParts = (List) list.get("file");

        InputPart inputPart;
        for (Iterator var6 = inputParts.iterator(); var6.hasNext(); stream = (InputStream) inputPart.getBody(InputStream.class, (Type) null)) {
            inputPart = (InputPart) var6.next();
        }

        List<InputPart> nombreParts = (List) list.get("id");

        InputPart inputPart2;
        for (Iterator var10 = nombreParts.iterator(); var10.hasNext(); nombre = inputPart2.getBodyAsString()) {
            inputPart2 = (InputPart) var10.next();
        }

        if (stream != null) {
            try {
                this.proccessStream(stream, nombre);
            } catch (NotSupportedException ex) {
                Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void proccessStream(InputStream stream, String nombre) throws IOException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        TypedQuery<Alerta> query = em.createQuery("SELECT c FROM Alerta c WHERE c.codigo = :cd", Alerta.class);
        query.setParameter("cd", nombre);

        Alerta alert = query.getSingleResult();
        transaction.begin();
        Audiomensaje audio = new Audiomensaje();
        audio.setIdAlerta(alert);
        audio.setFecha(new Date());
        em.persist(audio);
        em.flush();
        transaction.commit();

        FileOutputStream os = null;

        File fileToUpload = null;
        try {

            fileToUpload = new File("/opt/audios/" + alert.getIdalerta() + audio.getIdAudioMensaje() + ".ogg");

            os = new FileOutputStream(fileToUpload);
            byte[] b = new byte[2048];

            int length;
            while ((length = stream.read(b)) != -1) {
                os.write(b, 0, length);
            }

        } catch (IOException var15) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, (String) null, var15);
        } finally {
            os.close();
        }

    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Alerta entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Alerta find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Alerta> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Alerta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("estatus/{secret}")
    @Produces({MediaType.TEXT_PLAIN})
    public String retrieveEstatus(@PathParam("secret") String secret) {
        TypedQuery<Alerta> query = em.createQuery("SELECT c FROM Alerta c WHERE c.codigo = :key", Alerta.class);
        query.setParameter("key", secret);
        return query.getSingleResult().getEstatus();
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("share/{secret}")
    @Consumes({MediaType.TEXT_PLAIN})
    public void createMensaje(@PathParam("secret") String secret, String content) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            TypedQuery<Alerta> query = em.createQuery("SELECT c FROM Alerta c WHERE c.codigo = :codigo", Alerta.class);
            query.setParameter("codigo", secret);

            Alerta alert = query.getSingleResult();
            Paciente pc = alert.getPacienteIdpaciente();

            transaction.begin();
            Audiomensaje record = new Audiomensaje();
            record.setIdAlerta(alert);
            record.setMensaje(content);
            em.persist(record);
            transaction.commit();

            if (alert.getTipoalertaIdtipoalerta().getIdtipoalerta() == 1) {
                System.out.println("5");
                sendSMSGeneralHighAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), content, alert.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alert.getPacienteIdpaciente().getIdpaciente(), alert.getTipoalertaIdtipoalerta().getIdtipoalerta(), alert.getLat(), alert.getLng());
            } else {
                System.out.println("6");
                sendSMSGeneralLOWAlert(pc.getEntidadmedicaIdentidadmedica().getTelefono(), content, alert.getIdalerta(), pc.getEntidadmedicaIdentidadmedica().getNombre(), pc.getNombre(), alert.getPacienteIdpaciente().getIdpaciente(), alert.getTipoalertaIdtipoalerta().getIdtipoalerta(), alert.getLat(), alert.getLng());
            }
        } catch (NotSupportedException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(AlertaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private void sendSMS(String content, String phoneNumber) {
        System.out.println("llegue al target.....");

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                "MG8641e7db00ce0bd46ef9ac76e61c0eba",
                content)
                .create();
        System.out.println(message.getSid());
    }

    private void sendSMSNotificationsHighAlert(String telHospital, String secret, Integer alertaId, String hospitalName, String nombre, Integer idpaciente, Integer idtipoalerta, String lat, String log) {
        managedThreadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                TypedQuery<Contacto> query = em.createQuery("SELECT c FROM Contacto c WHERE c.pacienteIdpaciente.idpaciente = :id", Contacto.class);
                query.setParameter("id", idpaciente);
                List<Contacto> contactList = query.getResultList();
                String content = null;
                String base_ubicacion = "http://maps.google.com/maps?z=12&t=m&q=loc:" + lat + "+" + log;
                if (!contactList.isEmpty()) {
                    for (Contacto contacto : contactList) {
                        switch (contacto.getTipocontactoIdtipocontacto().getIdtipocontacto()) {
                            case ContactoFacadeREST.FAMILY_MEMBER:
                                content = "Your family member: " + nombre + " has requested medical assistance.  Location: " + base_ubicacion;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.GOVERMENT_SERVICE:
                                content = nombre + " Has requested medical assistance to : " + hospitalName + "  Location: " + base_ubicacion + "  Archive: https://csm-2022.ny-2.paas.massivegrid.net/hackaton/alertaDetalleGeneral.xhtml?id=" + alertaId;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NEIGHTBOR:
                                content = "Your NEIGHTBOR: " + nombre + " has requested medical assistance.  Location: " + base_ubicacion;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NURSE:
                                content = "Your Patient: " + nombre + " Has requested medical assistance  " + " Location: " + base_ubicacion + "  Archive: https://csm-2022.ny-2.paas.massivegrid.net/hackaton/alertaDetalleGeneral.xhtml?id=" + alertaId;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.TRUST_CONTACT:
                                content = "Your family member: " + nombre + " has requested medical assistance.  Location: " + base_ubicacion + "  secret key: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                        }
                    }
                }
                content = "Patient: " + nombre + " Has requested medical assistance  " + " Location: " + base_ubicacion + "  Archive: https://csm-2022.ny-2.paas.massivegrid.net/hackaton/alertaDetalle.xhtml?id=" + alertaId;
                sendSMS(content, "+52" + telHospital);
            }
        }).start();
    }

    private void sendSMSNotificationsLOWAlert(String telHospital, String secret, Integer alertaId, String hospitalName, String nombre, Integer idpaciente, Integer idtipoalerta, String lat, String log) {
        managedThreadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                TypedQuery<Contacto> query = em.createQuery("SELECT c FROM Contacto c WHERE c.pacienteIdpaciente.idpaciente = :id", Contacto.class);
                query.setParameter("id", idpaciente);
                List<Contacto> contactList = query.getResultList();
                String content = null;
                String base_ubicacion = "http://maps.google.com/maps?z=12&t=m&q=loc:" + lat + "+" + log;
                if (!contactList.isEmpty()) {
                    for (Contacto contacto : contactList) {
                        System.out.println("este contacto es: " + contacto.getTipocontactoIdtipocontacto().getIdtipocontacto());
                        switch (contacto.getTipocontactoIdtipocontacto().getIdtipocontacto()) {
                            case ContactoFacadeREST.FAMILY_MEMBER:
                                content = "Your family member: " + nombre + " has requested medical assistance.  Location: " + base_ubicacion;

                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;

                            case ContactoFacadeREST.NEIGHTBOR:
                                content = "Your NEIGHTBOR: " + nombre + " has requested medical assistance.  Location: " + base_ubicacion;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NURSE:
                                content = "Your Patient: " + nombre + " Has requested medical assistance  " + " Location: " + base_ubicacion + "  Archive: https://csm-2022.ny-2.paas.massivegrid.net/hackaton/alertaDetalleGeneral.xhtml?id=" + alertaId;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;

                        }
                    }
                    System.out.println("uno Content: " + content);
                } else {
                    System.out.println("dos Content: " + content);
                    sendSMSNotificationsHighAlert(telHospital, secret, alertaId, hospitalName, nombre, idpaciente, idtipoalerta, lat, log);
                }

            }
        }).start();
    }

    private void sendSMSGeneralHighAlert(String telHospital, String secret, Integer alertaId, String hospitalName, String nombre, Integer idpaciente, Integer idtipoalerta, String lat, String log) {
        managedThreadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                TypedQuery<Contacto> query = em.createQuery("SELECT c FROM Contacto c WHERE c.pacienteIdpaciente.idpaciente = :id", Contacto.class);
                query.setParameter("id", idpaciente);
                List<Contacto> contactList = query.getResultList();
                String content = null;

                if (!contactList.isEmpty()) {
                    for (Contacto contacto : contactList) {
                        switch (contacto.getTipocontactoIdtipocontacto().getIdtipocontacto()) {
                            case ContactoFacadeREST.FAMILY_MEMBER:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.GOVERMENT_SERVICE:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NEIGHTBOR:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NURSE:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.TRUST_CONTACT:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                        }
                    }
                }
                content = "Patient: " + nombre + " Says: " + secret;
                sendSMS(content, "+52" + telHospital);
            }
        }).start();
    }

    private void sendSMSGeneralLOWAlert(String telHospital, String secret, Integer alertaId, String hospitalName, String nombre, Integer idpaciente, Integer idtipoalerta, String lat, String log) {
        managedThreadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                TypedQuery<Contacto> query = em.createQuery("SELECT c FROM Contacto c WHERE c.pacienteIdpaciente.idpaciente = :id", Contacto.class);
                query.setParameter("id", idpaciente);
                List<Contacto> contactList = query.getResultList();
                String content = null;
                String base_ubicacion = "http://maps.google.com/maps?z=12&t=m&q=loc:" + lat + "+" + log;
                if (!contactList.isEmpty()) {
                    for (Contacto contacto : contactList) {
                        System.out.println("este contacto es: " + contacto.getTipocontactoIdtipocontacto().getIdtipocontacto());
                        switch (contacto.getTipocontactoIdtipocontacto().getIdtipocontacto()) {
                            case ContactoFacadeREST.FAMILY_MEMBER:
                                content = "Patient: " + nombre + " Says: " + secret;

                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;

                            case ContactoFacadeREST.NEIGHTBOR:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;
                            case ContactoFacadeREST.NURSE:
                                content = "Patient: " + nombre + " Says: " + secret;
                                sendSMS(content, "+52" + contacto.getTelefono());
                                break;

                        }
                    }
                    System.out.println("uno Content: " + content);
                } else {
                    System.out.println("dos Content: " + content);
                    sendSMSGeneralHighAlert(telHospital, secret, alertaId, hospitalName, nombre, idpaciente, idtipoalerta, lat, log);
                }

            }
        }).start();
    }

}

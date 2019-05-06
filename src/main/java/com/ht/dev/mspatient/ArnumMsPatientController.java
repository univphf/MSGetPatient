package com.ht.dev.mspatient;

import com.ht.dev.mspatient.models.Identite;
import com.ht.dev.mspatient.models.Jeton;
import com.ht.dev.mspatient.models.LoginDTO;
import com.ht.dev.mspatient.models.Patient;
import com.ht.dev.mspatient.models.No_Content;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * mai 2017
 * @author tondeur-h
 */
@RestController
@EnableSwagger2
public class ArnumMsPatientController {
    
      //verification de la cle publique du site demandeur
      List<Identite> idts = new ArrayList<>();
      List<No_Content> info=new ArrayList<>();
      
      //utiliser les JdbcTemplate => inserer la dépendance spring-boot-starter-jdbc
      @Autowired
      JdbcTemplate jdbcTemplate;
      
      //liste des paramétres externes
       @Value("${pathdoc}")
       private String pathdoc;
       
       @Value("${etablissement}")
       private String Etablisssment;
       
       @Value("${jwt.expirationtime}")
       long EXPIRATIONTIME;
       @Value("${jwt.secret}")
       String SECRET;
       static final String TOKEN_PREFIX = "Bearer";
       static final String HEADER_STRING = "Authorization";
    
      //logger 
      private static final Logger log = LoggerFactory.getLogger(ArnumMsPatientApplication.class);
      
      
     /******************************************
      * Connection utilisateur et 
      * retour d'un jeton JWT
      * Utilise le DTO LoginDTO pour encapsuler
      * du JSon et retourner le Jeton
     * @param identite
     * @return 
      ******************************************/ 
      @RequestMapping(value="/uphf/getjwt", method = RequestMethod.POST)
      public LoginDTO connect(@RequestBody Identite identite)
      {
          //TODO a remplacer par un appel LDAP ou sgbd
          boolean connexionOK=true;
          String login=identite.getIdentite();
          String password=identite.getPassword();
          
          LoginDTO logDTO=new LoginDTO();
          if (connexionOK)
          {
            //reponse OK
            logDTO.setConnectOK(true);
            //construire le Jeton JWT
            String JWT = Jwts.builder()
        .setSubject(login)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
            logDTO.setJeton(JWT);
          }
          else
          {
            logDTO.setConnectOK(false);
            logDTO.setJeton("empty");
          }
          //reponse JSon
          return logDTO;
      }
      
      
    /*********************************
     * Verifier un jeton 
     * et lire le nom de l'utilisateur
     * si non null alors le jeton est valide
     * @param jeton
     * @return 
     *********************************/  
    public boolean getValiditeDuJeton(String jeton) {
        Date expiration=null;
    if (jeton != null) {
      // parser le jeton et verifier la date d'expiration.
      expiration = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(jeton)
          .getBody()
          .getExpiration();
    }
    //si le jeton est encore valide
    return expiration.getTime()>System.currentTimeMillis();
  }
      
      
   /****************************************
    * permet de controller la valeur de la 
    * cle publique du client a titre de verification
    * pour le demandeur, s'il est connu des services
    * @param cle
    * @return 
    ****************************************/
    @RequestMapping(value="/uphf",method = RequestMethod.POST)
    private ResponseEntity identification(@RequestBody Jeton jeton)
    {
        log.info(jeton.getJeton());
        
        if (getValiditeDuJeton(jeton.getJeton())==false)
        {
            return new ResponseEntity("Votre jeton est incorrect",HttpStatus.OK);
        }
      
        //retourne une liste de valeur => normalement un seul objet.
         return new ResponseEntity("UPHC Valenciennes TH 2019",HttpStatus.OK);
    }
    

       /****************************************
    * permet de controller la valeur de la 
    * cle publique du client a titre de verification
    * pour le demandeur, s'il est connu des services
    * @param cle
    * @return 
    ****************************************/
    @RequestMapping(value="/uphf/header",method = RequestMethod.POST)
    private ResponseEntity identification2(@RequestHeader(value="Authorization") String jeton)
    {
     
        String BEARER="Bearer";
        
    if(jeton.startsWith(BEARER)) {jeton = jeton.substring(BEARER.length()+1);}
        
        log.info(jeton);
        
        if (getValiditeDuJeton(jeton)==false)
        {
            return new ResponseEntity("Votre jeton est incorrect",HttpStatus.OK);
        }
      
        //retourne une liste de valeur => normalement un seul objet.
         return new ResponseEntity("UPHF Valenciennes TH 2019",HttpStatus.OK);
    }

    
    /*********************************
     * recherche par IPP
     * @param cle
     * @param nom
     * @return 
     *********************************/
    @RequestMapping(value="/uphf/ipp={ipp}",method = RequestMethod.GET)
    private ResponseEntity<List<Patient>> getByIPP(@PathVariable("ipp") String ipp)
    { 
        List<Patient> patients=new ArrayList<>();
      
String sql="select IPP, NOM, PRENOM, DDN, TEL, EMAIL from pat WHERE IPP like '"+ipp+"%'";        
        
        List<Map<String,Object>> rows =jdbcTemplate.queryForList(sql);
        rows.forEach((row) -> {
            Patient p=new Patient();
            p.setIPP(row.get("IPP").toString());            
            p.setNOM(row.get("NOM").toString());
            p.setPRENOM(row.get("PRENOM").toString());
            p.setDDN(row.get("DDN").toString());
            p.setTEL(row.get("TEL").toString());
            p.setEMAIL(row.get("EMAIL").toString());
            patients.add(p);
          });
        
        //si la liste est vide
        if (patients.isEmpty()) {
             // info.add(new No_Content("NO CONTENT"));
             //return new ResponseEntity(info,HttpStatus.OK);
            return new ResponseEntity(HttpStatus.NOT_FOUND);  //pour être RESTfull
            //return new ResponseEntity(new CustomErrorType("Utilisateur avec le nom " + nom + " non trouvé"), HttpStatus.NOT_FOUND);
        }
        //sinon on retourne la liste
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    
    /*********************************
     * recherche par nom & prenom & DDN de patient
     * @param cle
     * @param nom
     * @return 
     *********************************/
    @RequestMapping(value="/uphf/nom={nom}/prenom={prenom}/ddn={ddn}",method = RequestMethod.GET)
    private ResponseEntity<List<Patient>> getByNameFirstNameDDN(@PathVariable("nom") String nom,@PathVariable("prenom") String prenom,@PathVariable("ddn") String ddn)
    {
 
        List<Patient> patients=new ArrayList<>();
       
        String sql="select IPP, NOM, PRENOM, DDN, TEL, EMAIL from pat WHERE prenom like '" + prenom + "%' and nom like '" + nom + "%' and ddn = STR_TO_DATE ('"+ ddn +"','%d%m%Y')";
   
        
        List<Map<String,Object>> rows =jdbcTemplate.queryForList(sql);
        rows.forEach((row) -> {
            Patient p=new Patient();
            p.setIPP(row.get("IPP").toString());            
            p.setNOM(row.get("NOM").toString());
            p.setPRENOM(row.get("PRENOM").toString());
            p.setDDN(row.get("DDN").toString());
            p.setTEL(row.get("TEL").toString());
            p.setEMAIL(row.get("EMAIL").toString());
            patients.add(p);
          });
        
        //si la liste est vide
        if (patients.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);  //pour être RESTfull
        }
        //sinon on retourne la liste
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    /**********************************
     * supprimer un patient
     * @param ipp
     * @return 
     **********************************/
    @RequestMapping(value="/uphf/{ipp}/delete",method = RequestMethod.DELETE)
    private ResponseEntity deleteByIPP(@PathVariable("ipp") String ipp)
    {
        
        String sql="delete from pat where ipp='"+ipp+"'";
        try{
        jdbcTemplate.execute(sql);
        } catch(DataAccessException dae)
        {
           info.add(new No_Content("NOT DELETED"));
        return new ResponseEntity(info,HttpStatus.OK); 
        }
        
        info.add(new No_Content("DELETED"));
        return new ResponseEntity(info,HttpStatus.OK);
    }
  
    /***********************************
     * Mise a jour du patient
     * @param patient
     * @return 
     ***********************************/
    @RequestMapping(value="/uphf/patient/update",method = RequestMethod.PUT)
    private ResponseEntity updatePatient(@RequestBody Patient patient)
    {
               
       String sql="Update Pat set nom='"+patient.getNOM()+"', prenom='"+patient.getPRENOM()+"', tel='"+patient.getTEL()+"', email='"+patient.getEMAIL()+"', ddn=STR_TO_DATE ('"+ patient.getDDN() +"','%d%m%Y') where ipp='"+patient.getIPP()+"'";
       //log.info(sql);
       try{
        jdbcTemplate.execute(sql);
        } catch(DataAccessException dae)
        {
           info.add(new No_Content("NOT UPDATED"));
        return new ResponseEntity(info,HttpStatus.OK); 
        }
        
        info.add(new No_Content("UPDATED"));
        return new ResponseEntity(info,HttpStatus.OK);
    }

    
    /*********************************
     * Creer un patient
     * @param patient
     * @return 
     *********************************/ 
    @RequestMapping(value="/uphf/patient/new",method = RequestMethod.POST)
    private ResponseEntity insertPatient(@RequestBody Patient patient)
    {
               
       String sql="insert into Pat(ipp,nom,prenom,tel,email,ddn) values ('"+patient.getIPP()+"','"+patient.getNOM()+"','"+patient.getPRENOM()+"','"+patient.getTEL()+"','"+patient.getEMAIL()+"',STR_TO_DATE ('"+ patient.getDDN()+"','%d%m%Y'))";
       log.info(sql);
       try{
        jdbcTemplate.execute(sql);
        } catch(DataAccessException dae)
        {
           info.add(new No_Content("NOT CREATED"));
        return new ResponseEntity(info,HttpStatus.OK); 
        }
        
        info.add(new No_Content("CREATED"));
        return new ResponseEntity(info,HttpStatus.OK);
    }
       
}

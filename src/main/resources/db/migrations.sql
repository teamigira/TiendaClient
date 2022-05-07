/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Nkanabo
 * Created: May 3, 2022
 */

CREATE TABLE Updates (
    releaseId BIGINT NOT NULL,
    release_note varchar(255),
    release_date varchar(255),
    bugs varchar(255),
    consumers varchar(255)
);
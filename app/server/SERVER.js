const fastify = require("fastify")();
const fs = require('fs');
const port = 3000;



// #######################################
// DEFAULT
// #######################################
fastify.get("/", (req, res) => {
  res.send("Welcome to DeliverEase API!");
});







// #######################################
// OPENING CONNECTION
// #######################################
fastify.listen(port, () => {
  console.log(`Server running on port ${port}.`);
});






// #######################################
// USERS
// #######################################
// POST -> create new user
fastify.post("/users", (req, res) => {
  const parsedData = JSON.parse(req.body);

  
  // Read the existing JSON file
  const jsonData = fs.readFileSync('users.json', 'utf8');
  const data = JSON.parse(jsonData);

  // Create a new entry
  const newEntry = {
    id: parsedData.id,
    name: parsedData.name,
    surname: parsedData.surname,
    email: parsedData.email,
    password: parsedData.password,
    permanentConstraints: parsedData.permanentConstraints,
    nonPermanentConstraints: parsedData.nonPermanentConstraints
  };
  
  
  console.log('JSON data :', data);



  // Find the user by ID or create a new one
  const userIndex = data.users.findIndex(user => user.id === parsedData.id);

  // Update the user if found, otherwise add a new user
  if (userIndex !== -1) {
    data.users[userIndex] = newEntry;
  } else {
    data.users.push(newEntry);
  }

  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);
  console.log('NEW USER JSON data:', updatedJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('users.json', updatedJsonData, 'utf8');

  res.send(`User ${parsedData.name} ${parsedData.name} with email ${parsedData.email}, password ${parsedData.password} and ID ${parsedData.id} created succesfully`);
});


// GET -> retrieving all users
fastify.get("/users", (req, res) => {  
  // Read the JSON file
  fs.readFile('users.json', 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      return;
    }

    try {
      // Parse the JSON data
      const jsonData = JSON.parse(data);
      console.log('JSON data:', jsonData);
      
      // Sending the response to the client
      res.send(jsonData);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  });
});


// DELETE -> deleting a user
fastify.delete("/users", (req, res) => {
  const parsedData = JSON.parse(req.body);
  
  // Read the existing JSON file
  const jsonData = fs.readFileSync('users.json', 'utf8');
  const data = JSON.parse(jsonData);
  
  console.log('JSON data :', data);
  
  // Find the user by ID
  const userIndex = data.users.findIndex(user => user.id === parsedData.id);

  
  // Update the user if found, otherwise add a new user
  if (userIndex !== -1) {
    // Remove the object from the JavaScript object
    data.users.splice(userIndex, 1);
    
    deleteShiftsOfUser(parsedData.id)
    
    deleteMessagesOfUser(parsedData.id)
  }

  
  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);
  console.log('NEW USER JSON data:', updatedJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('users.json', updatedJsonData, 'utf8');

  res.send(`User ${parsedData.name} ${parsedData.name} with email ${parsedData.email}, password ${parsedData.password} and ID ${parsedData.id} created succesfully`);
});


function deleteShiftsOfUser(id) {
  // remove the shifts of rider in days
  const calendarJsonData = fs.readFileSync('calendar.json', 'utf8');
  const calendarData = JSON.parse(calendarJsonData);

  calendarData.days.forEach((day) => {
      const idIndex = day.riders.findIndex(r => r === id);

      console.log('DAY: ', day.date, 'IDX: ', idIndex)

      if (idIndex !== -1){
        day.riders.splice(idIndex, 1)
      }
    }
  )

  // Convert the updated data object back to JSON
  const updatedCalendarJsonData = JSON.stringify(calendarData, null, 2);

  console.log('NEW CALENDAR JSON data:', updatedCalendarJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('calendar.json', updatedCalendarJsonData, 'utf8');
}


function deleteMessagesOfUser(id) {
  // remove the shifts of rider in days
  const messagesJsonData = fs.readFileSync('messages.json', 'utf8');
  const messagesData = JSON.parse(messagesJsonData);

  // retrieve all messages not sent by the id specified
  messagesData.messages = messagesData.messages.filter((m) => (m.senderID !== id && m.type !== "REQUEST"))

  // Convert the updated data object back to JSON
  const updatedMessagesJsonData = JSON.stringify(messagesData, null, 2);

  console.log('NEW MESSAGES JSON data:', updatedMessagesJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('messages.json', updatedMessagesJsonData, 'utf8');
}





// #######################################
// MESSAGES
// #######################################
// POST -> add new message
fastify.post("/messages", (req, res) => {
  const parsedData = JSON.parse(req.body);
  
  // Read the existing JSON file
  const jsonData = fs.readFileSync('messages.json', 'utf8');
  const data = JSON.parse(jsonData);

  // Create a new entry
  const newEntry = {
    id: parsedData.id,
    senderID: parsedData.senderID,
    receiverID: parsedData.receiverID,
    body: parsedData.body,
    type: parsedData.type,
    date: parsedData.date,
  };

  // Add the new entry to the data object
  data.messages.push(newEntry);
  
  
  
  // shift updating in case of change
  if(parsedData.type == "ACCEPTANCE"){
    const jsonCalendarData = fs.readFileSync('calendar.json', 'utf8');
    const calendarData = JSON.parse(jsonCalendarData);
    
    
    const messageAcceptedIndex = data.messages.findIndex(m => m.id === parsedData.body);
    const messageAccepted = data.messages[messageAcceptedIndex]
    
    
    const dayOffered = messageAccepted.body.split('#')[0];
    const dayWanted = messageAccepted.body.split('#')[1];
    const riderWhoProposed = parsedData.receiverID;
    const riderWhoAccepted = parsedData.senderID;
    
    
    // Find by ID or create a new one
    const dayOfferedIndex = calendarData.days.findIndex(day => day.date === dayOffered);
    const dayWantedIndex = calendarData.days.findIndex(day => day.date === dayWanted);

    
    // Update the days if founds
    if (dayOfferedIndex !== -1 && dayWantedIndex != -1) {
      const riderWhoAcceptedIndex = calendarData.days[dayWantedIndex].riders.findIndex(id => id === riderWhoAccepted);
      const riderWhoProposedIndex = calendarData.days[dayOfferedIndex].riders.findIndex(id => id === riderWhoProposed);
      
      
      if (riderWhoAcceptedIndex !== -1 && riderWhoProposedIndex != -1) {
        calendarData.days[dayWantedIndex].riders[riderWhoAcceptedIndex] = riderWhoProposed
        calendarData.days[dayOfferedIndex].riders[riderWhoProposedIndex] = riderWhoAccepted
      }
      
    
      // Convert the updated data object back to JSON
      const updatedJsonCalendarData = JSON.stringify(calendarData, null, 2);
      
      console.log('NEW CALENDAR JSON data:', updatedJsonCalendarData);
      
      // Write the JSON data back to the file
      fs.writeFileSync('calendar.json', updatedJsonCalendarData, 'utf8');
    }
    
    
    // rimuovi il messaggio di richiesta
    if(messageAcceptedIndex !== -1){
      data.messages.splice(messageAcceptedIndex, 1)
    }
  }
  
  
  

  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);
  
  console.log('NEW MESSAGE JSON data:', updatedJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('messages.json', updatedJsonData, 'utf8');
  
  
  res.send(`New message with senderID: ${parsedData.senderID}, receiverID: ${parsedData.receiverID}, message: ${parsedData.body} and type: ${parsedData.type}`);
  
});


// GET -> retrieve messages
fastify.get("/messages", (req, res) => {
  // Read the JSON file
  fs.readFile('messages.json', 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      return;
    }

    try {
      // Parse the JSON data
      const jsonData = JSON.parse(data);
      console.log('JSON data:', jsonData);
      
      // Sending the response to the client
      res.send(jsonData);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  });
});


// GET -> retrieve messages of single user
fastify.get("/messagesOf", (req, res) => {
  const receiverID = req.query.receiverID
  
  var jsonData = null;
  // Read the JSON file
  fs.readFile('messages.json', 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      return;
    }

    try {
      // Parse the JSON data
      const jsonData = JSON.parse(data);

      // Filter messages by receiverID
      const filteredMessages = jsonData.messages.filter(
        (message) => message.receiverID === receiverID
      );

      res.send(filteredMessages);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  });
});





// #######################################
// CALENDAR
// #######################################
// GET -> retrieve days
fastify.get("/calendar", (req, res) => {
  // Read the JSON file
  fs.readFile('calendar.json', 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      return;
    }

    try {
      // Parse the JSON data
      const jsonData = JSON.parse(data);
      console.log('JSON data:', jsonData);
      
      // Sending the response to the client
      res.send(jsonData);
    } catch (error) {
      console.error('Error parsing JSON:', error);
    }
  });
});

// POST -> add days
fastify.post("/calendar", (req, res) => {
  const parsedData = JSON.parse(req.body);
  
  // Read the existing JSON file
  const jsonData = fs.readFileSync('calendar.json', 'utf8');
  const data = JSON.parse(jsonData);
  
  
  // to add/update multiple dates
  if(Array.isArray(parsedData)){
    const bodyData = parsedData;
    
    bodyData.forEach((day) => {
      // Create a new entry
      const newEntry = {
        date: day.date,
        riders: day.riders
      };



      // Find the user by ID or create a new one
      const dayIndex = data.days.findIndex(cday => cday.date === day.date);

      // Update the user if found, otherwise add a new user
      if (dayIndex !== -1) {
        data.days[dayIndex] = newEntry;
      } else {
        data.days.push(newEntry);
      }
    });
  // to add/update single dates
  } else {
    // Create a new entry
    const newEntry = {
      date: parsedData.date,
      riders: parsedData.riders
    };



    // Find the user by ID or create a new one
    const dayIndex = data.days.findIndex(day => day.date === parsedData.date);

    // Update the user if found, otherwise add a new user
    if (dayIndex !== -1) {
      data.days[dayIndex] = newEntry;
    } else {
      data.days.push(newEntry);
    }
  }
  
  
  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);
  
  console.log('NEW CALENDAR JSON data:', updatedJsonData);

  // Write the JSON data back to the file
  fs.writeFileSync('calendar.json', updatedJsonData, 'utf8');

  res.send(`New day added: ${parsedData.date}, riders: ${parsedData.ridersID}`);
  
});






// #######################################
// TEST
// #######################################
fastify.post("/test", (req, res) => {
  const parsedData = JSON.parse(req.body);

  res.send(`${parsedData}`);
});




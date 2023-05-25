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
    password: parsedData.password
  };
  
  console.log('JSON data:', data);

  // Add the new entry to the data object
  data.users.push(newEntry);

  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);
  console.log('NEW JSON data:', updatedJsonData);

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
    senderID: parsedData.senderID,
    receiverID: parsedData.receiverID,
    body: parsedData.body,
    type: parsedData.type,
    date: parsedData.date,
  };

  // Add the new entry to the data object
  data.messages.push(newEntry);

  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);

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


// GET -> retrieve messages of dingle user
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

  // Create a new entry
  const newEntry = {
    date: parsedData.date,
    ridersID: parsedData.ridersID
  };

  // Add the new entry to the data object
  data.messages.push(newEntry);

  // Convert the updated data object back to JSON
  const updatedJsonData = JSON.stringify(data, null, 2);

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




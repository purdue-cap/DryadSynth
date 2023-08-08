use openai::chat::{ChatCompletionMessage, ChatCompletion, ChatCompletionBuilder, ChatCompletionMessageRole::{Assistant, User}};


struct ChatGPTI {
    system: Vec<ChatCompletionMessage>,
    prmt1: Vec<ChatCompletionMessage>,
    prmt2: Vec<ChatCompletionMessage>,
}

impl ChatGPTI {
    
    pub fn get_msg(&self) -> Vec<ChatCompletionMessage>  {
        [self.system.clone(), self.prmt1.clone(), self.prmt2.clone()].concat()
    }
    pub async fn run(&mut self, b: bool) -> String {
        let response = loop {
            let result = ChatCompletion::builder("gpt-3.5-turbo", self.get_msg()).temperature(0.5).create().await;
            if let Ok(Ok(a)) = result {
                break a.choices[0].message.content.clone();
            } else {
                self.prmt2.remove(0);
                self.prmt2.remove(0);
            }
        };
        if b {
            self.prmt1.push(ChatCompletionMessage { role: Assistant, content: response.clone(), name: None })
        } else {
            self.prmt2.push(ChatCompletionMessage { role: Assistant, content: response.clone(), name: None })
        }
        response
    }
    pub fn ask1(&mut self, prompt: String) {
        self.prmt1.push(ChatCompletionMessage { role: User, content: prompt, name: None })
    }
    pub fn ask(&mut self, prompt: String) {
        self.prmt2.push(ChatCompletionMessage { role: User, content: prompt, name: None })
    }
}
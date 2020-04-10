import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";
import CreateProjectButton from "./Project/CreateProjectButton";
import { connect } from "react-redux";
import { getProjects } from "../actions/projectActions";
import propTypes from "prop-types";

class Dashboard extends Component {
  componentDidMount() {
    this.props.getProjects();
  }

  render() {
    // const projectObject = {
    //   projectName: "Project name from props",
    //   projectIdentifier: "Project identifier from props",
    //   description: "Description name props",
    // };
    const { projects } = this.props.project;
    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Projects</h1>
              <br />
              <CreateProjectButton />
              <br />
              <hr />
              {projects.map((project) => (
                <ProjectItem key={project.id} project={project} />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

Dashboard.propTypes = {
  project: propTypes.object.isRequired,
  getProjects: propTypes.func.isRequired,
};

const mapStateToProps = (state) => ({
  project: state.project,
});
export default connect(mapStateToProps, { getProjects })(Dashboard);
